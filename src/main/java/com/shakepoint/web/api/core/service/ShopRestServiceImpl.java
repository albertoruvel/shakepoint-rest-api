package com.shakepoint.web.api.core.service;

import com.shakepoint.integration.jms.client.handler.JmsHandler;
import com.shakepoint.web.api.core.machine.ProductType;
import com.shakepoint.web.api.core.machine.PurchaseStatus;
import com.shakepoint.web.api.core.repository.*;
import com.shakepoint.web.api.core.service.email.EmailAsyncSender;
import com.shakepoint.web.api.core.service.email.Template;
import com.shakepoint.web.api.core.service.promo.PromoCodeManager;
import com.shakepoint.web.api.core.service.security.AuthenticatedUser;
import com.shakepoint.web.api.core.service.security.RequestPrincipal;
import com.shakepoint.web.api.core.service.security.SecurityRole;
import com.shakepoint.web.api.core.shop.PayWorksClientService;
import com.shakepoint.web.api.core.util.ShakeUtils;
import com.shakepoint.web.api.core.util.TransformationUtils;
import com.shakepoint.web.api.data.dto.request.ConfirmPurchaseRequest;
import com.shakepoint.web.api.data.dto.request.UserProfileRequest;
import com.shakepoint.web.api.data.dto.response.*;
import com.shakepoint.web.api.data.entity.*;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.*;

public class ShopRestServiceImpl implements ShopRestService {


    @Inject
    private UserRepository userRepository;

    @Inject
    private MachineRepository machineRepository;

    @Inject
    private PurchaseRepository purchaseRepository;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private JmsHandler jmsHandler;

    @Inject
    private EmailAsyncSender emailSender;

    @Inject
    private PayWorksClientService payWorksClientService;

    @Inject
    private PromoCodeRepository promoCodeRepository;

    @Inject
    private PromoCodeManager promoCodeManager;

    @Inject
    @AuthenticatedUser
    private RequestPrincipal authenticatedUser;

    private final Logger LOG = Logger.getLogger(getClass());
    private static final String RETRY_UPLOAD_QUEUE_NAME = "retry_file_upload";

    @Override
    public Response searchMachine(double longitude, double latitude) {
        //get all machines
        List<VendingMachine> machines = machineRepository.getMachines();
        LOG.info(String.format("Found %d registered machines", machines.size()));
        MachineSearch search = new MachineSearch();
        double distance = 1000000; // high distance to get accurate results
        String[] array = null;
        int currentIndex = 0;
        for (int i = 0; i < machines.size(); i++) {
            if (machines.get(i).getLocation() == null || machines.get(i).getLocation().isEmpty())
                continue;
            array = machines.get(i).getLocation().split(",");
            double long1 = Double.parseDouble(array[0]);
            double lat1 = Double.parseDouble(array[1]);
            //get distance
            double tmpDistance = distance(lat1, long1, latitude, longitude);
            if (tmpDistance < distance) {
                distance = tmpDistance;
                currentIndex = i;
            }

        }
        VendingMachine machine = machines.get(currentIndex);
        LOG.info(String.format("Got machine %s from distance search", machine.getName()));
        search.setMachineId(machine.getId());
        search.setMachineName(machine.getName());
        search.setDistance(distance);
        return Response.ok(search).build();
    }

    @Override
    public Response getMachineProducts(String machineId) {
        if (machineId == null || machineId.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<VendingMachineProductStatus> productsStatus = machineRepository.getMachineProducts(machineId);
        List<Product> products = new ArrayList();
        for (VendingMachineProductStatus p : productsStatus) {
            products.add(p.getProduct());
        }
        List<ProductDTO> productsDTO = TransformationUtils.createProducts(products);
        return Response.ok(productsDTO).build();
    }

    @Override
    public Response getActiveQrCodes(String machineId) {
        //get user id
        List<PurchaseCodeResponse> page = TransformationUtils.createPurchaseCodes(purchaseRepository.getAuthorizedPurchases(authenticatedUser.getId(), machineId));
        return Response.ok(page).build();
    }

    @Override
    public Response confirmPurchase(ConfirmPurchaseRequest request) {
        Purchase purchase = purchaseRepository.getPurchase(request.getPurchaseId());
        User user = userRepository.get(authenticatedUser.getId());
        if (purchase == null) {
            LOG.error(String.format("No purchase found for %s", request.getPurchaseId()));
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new PurchaseQRCode(null, false, "No se ha podido encontrar la compra para la máquina que se especificó"))
                    .build();
        } else if (purchase.getStatus() == PurchaseStatus.AUTHORIZED || purchase.getStatus() == PurchaseStatus.CASHED) {
            //trying to buy  a purchase with another status
            LOG.info("Trying to buy an already authorized or cashed purchase");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new PurchaseQRCode(null, false, "La compra especificada ya ha sido comprada por alguien mas, refresca los productos y vuelve a intentar"))
                    .build();
        } else {
            //get promo code if exists
            if (request.getPromoCode() != null && !request.getPromoCode().isEmpty()) {
                //get it
                PromoCode promoCode = promoCodeRepository.findPromoCodeByCode(request.getPromoCode());
                if (promoCode == null) {
                    //promo code is invalid
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(new PurchaseQRCode(null, false, "El código de promoción es inválido")).build();
                } else {
                    //check expiration date for promo code
                    if (promoCodeManager.isPromoCodeExpired(promoCode)) {
                        //expired
                        return Response.status(Response.Status.BAD_REQUEST)
                                .entity(new PurchaseQRCode(null, false, "El código de promoción ha expirado")).build();
                    } else {
                        //check if user already redeemed promo code
                        if (promoCodeRepository.isPromoCodeAlreadyRedeemedByUser(promoCode.getCode(), user.getId())) {
                            return Response.status(Response.Status.BAD_REQUEST)
                                    .entity(new PurchaseQRCode(null, false, "El código de promoción ya ha sido canjeado anteriormente")).build();
                        } else {
                            //set new price for purchase
                            purchase.setTotal(calculatePurchaseTotal(purchase.getTotal(), promoCode.getDiscount()));
                            //call pay works to make payment
                            return confirmPurchase(purchase, request, user, promoCode);
                        }
                    }
                }
            } else {
                return confirmPurchase(purchase, request, user, null);
            }

        }
    }

    private double calculatePurchaseTotal(double total, int discount) {
        if (discount == 100) {
            //free drink!! :D
            return 0;
        } else {
            double discDec = discount / 100;
            double discValue = total * discDec;
            return total - discValue;
        }
    }

    private Response confirmPurchase(Purchase purchase, ConfirmPurchaseRequest request, User user, PromoCode promoCode) {
        final String controlNumber = createControlNumber();
        purchase.setControlNumber(controlNumber);
        //update purchase
        purchaseRepository.update(purchase);
        PaymentDetails paymentDetails = payWorksClientService.authorizePayment(request.getCardNumber(),
                request.getCardExpirationDate(), request.getCvv(), purchase.getTotal(), purchase.getControlNumber());
        if (paymentDetails == null) {
            LOG.info("No payment details from pay works");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new PurchaseQRCode(null, false, "Ha ocurrido un problema al realizar el pago, intenta nuevamente"))
                    .build();
        } else if (paymentDetails.getAuthCode() != null && paymentDetails.getPayworksResult().equals("A")) {
            //check if purchase already contains a qr code
            if (purchase.getQrCodeUrl() == null) {
                //try to retry upload for purchase
                jmsHandler.send(RETRY_UPLOAD_QUEUE_NAME, purchase.getId());
            }
            //payment went well
            purchase.setUser(user);
            purchase.setStatus(PurchaseStatus.AUTHORIZED);
            purchase.setReference(paymentDetails.getReference());
            purchaseRepository.update(purchase);

            if (user.getRole().equals(SecurityRole.MEMBER.getValue())) {
                //check how many purchases member has
                Integer purchasesCount = purchaseRepository.getUserPurchases(user.getId()).size();
                if (purchasesCount != 0 && purchasesCount % 10 == 0) {
                    //multiple of 10, means it deserves another drink for 50%
                    //create a new promo code for this unique user
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.DAY_OF_YEAR, 7);
                    PromoCode newUserPromoCode = promoCodeManager.createPromoCode(ShakeUtils.SIMPLE_DATE_FORMAT.format(calendar.getTime()),
                            "Te has ganado una bebida gratis!", 50, PromoType.EARNED);
                    promoCodeRepository.createPromoCode(newUserPromoCode);

                    Map<String, Object> earnedDiscountEmailParams = new HashMap<>();

                    emailSender.sendEmail(user.getEmail(), Template.EARNED_DRINK_DISCOUNT, earnedDiscountEmailParams);
                }
            }

            Map<String, Object> emailParams = new HashMap<String, Object>();
            emailParams.put("productName", purchase.getProduct().getName());

            if (promoCode != null) {
                emailParams.put("promoCode", promoCode.getCode());
                emailParams.put("promoDescription", promoCode.getDescription());
                //means user used promo code to pay this product
                //create promo code redemption
                PromoCodeRedeem redemption = promoCodeManager.createPromoCodeRedemption(promoCode, user);
                //add to database
                promoCodeRepository.redeemPromoCode(redemption);
                //send email
                emailSender.sendEmail(user.getEmail(), Template.SUCCESSFUL_PROMO_PURCHASE, emailParams);

                //check if promo code contains a trainer assigned
                User trainer = promoCode.getTrainer();
                if (trainer != null) {
                    //check number of redeems for promo code
                    Integer redemptionsCount = promoCodeRepository.getPromoCodeRedemptions(promoCode.getCode());
                    if (redemptionsCount % 10 == 0) {
                        //deserves 10% off on next drink
                        //TODO: creates a new promo code
                        //PromoCode trainerPromoCode = promoCodeManager.createPromoCode()
                    }
                }

                return Response.ok(new PurchaseQRCode(purchase.getQrCodeUrl(), true, paymentDetails.getComputedMessage())).build();
            } else {
                //send standard email
                emailSender.sendEmail(user.getEmail(), Template.SUCCESSFUL_PURCHASE, emailParams);
                return Response.ok(new PurchaseQRCode(purchase.getQrCodeUrl(), true, paymentDetails.getComputedMessage())).build();
            }
        } else if (paymentDetails.getPayworksResult().equals("D")) {
            //declined
            LOG.info("Declined");
            return Response.ok(new PurchaseQRCode(null, false, paymentDetails.getComputedMessage()))
                    .build();
        } else if (paymentDetails.getPayworksResult().equals("T")) {
            LOG.info("Timeout on provider");
            return Response.ok(new PurchaseQRCode(null, false, paymentDetails.getComputedMessage())).build();
        } else {
            LOG.info("Rejected");
            return Response.ok(new PurchaseQRCode(null, false, paymentDetails.getComputedMessage())).build();
        }
    }

    @Override
    public Response getUserPurchases() {
        List<Purchase> purchases = purchaseRepository.getUserPurchases(authenticatedUser.getId());
        List<UserPurchaseResponse> response = TransformationUtils.createPurchases(purchases);
        return Response.ok(response).build();
    }

    @Override
    public Response getUserProfile() {
        final User user = userRepository.get(authenticatedUser.getId());
        UserProfileResponse profile = null;
        try {
            UserProfile userProfile = userRepository.getUserProfile(user.getId());
            if (userProfile == null) {
                profile = new UserProfileResponse(user.getName(), user.getId(), user.getCreationDate(), false, null, 0.0, 0.0, 0.0, user.getEmail());
            } else {
                profile = TransformationUtils.createUserProfile(userProfile);
            }
        } catch (Exception ex) {

        }
        return Response.ok(profile).build();
    }

    @Override
    public Response saveProfile(UserProfileRequest request) {
        //get user id
        final User user = userRepository.get(authenticatedUser.getId());
        //get profile
        UserProfile existingProfile = userRepository.getUserProfile(authenticatedUser.getId());
        if (existingProfile == null) {
            UserProfile profile = TransformationUtils.getProfile(authenticatedUser.getId(), request);
            profile.setUser(user);
            LOG.info("Creating user profile");
            userRepository.saveProfile(profile);
        } else {
            existingProfile.setBirthday(request.getBirthday());
            existingProfile.setHeight(request.getHeight());
            existingProfile.setWeight(request.getWeight());
            LOG.info("Updating user profile");
            userRepository.updateProfile(existingProfile);
        }

        return getUserProfile();
    }

    @Override
    public Response searchMachinesByName(String machineName) {
        List<VendingMachine> machines = machineRepository.searchByName(machineName);
        List<MachineSearch> machineSearches = new ArrayList();
        for (VendingMachine m : machines) {
            machineSearches.add(new MachineSearch(m.getId(), m.getName(), 0));
        }
        return Response.ok(machineSearches).build();
    }

    @Override
    public Response getProductDetails(String productId) {
        Product product = productRepository.getProduct(productId);
        ProductDTO dto = new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getDescription(),
                product.getLogoUrl(), ProductType.getProductTypeForClient(product.getType()), product.getNutritionalDataUrl());
        return Response.ok(dto).build();
    }

    @Override
    public Response getAvailablePurchaseForMachine(String productId, String machineId) {
        //get available products
        List<Purchase> purchases = purchaseRepository.getAvailablePurchasesForMachine(productId, machineId);
        LOG.info(String.format("Got a total of %d available purchases", purchases.size()));
        if (purchases.isEmpty()) {
            return Response.ok(new AvailablePurchaseResponse(null)).build();
        } else {
            //get the first one
            return Response.ok(new AvailablePurchaseResponse(purchases.get(0).getId())).build();
        }
    }


    /**
     * Get distance in mts from 2 points
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @param
     * @return
     */
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return (dist);
    }


    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts decimal degrees to radians						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts radians to decimal degrees						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private String createControlNumber() {
        final String controlNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        return controlNumber;
    }
}
