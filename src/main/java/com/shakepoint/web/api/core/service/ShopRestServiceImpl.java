package com.shakepoint.web.api.core.service;

import com.github.roar109.syring.annotation.ApplicationProperty;
import com.google.gson.Gson;
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
import com.shakepoint.web.api.data.dto.request.ContactRequest;
import com.shakepoint.web.api.data.dto.request.FcmTokenRequest;
import com.shakepoint.web.api.data.dto.request.UserProfileRequest;
import com.shakepoint.web.api.data.dto.request.ValidatePromoCodeRequest;
import com.shakepoint.web.api.data.dto.response.*;
import com.shakepoint.web.api.data.entity.*;
import com.shakepoint.web.api.data.fcm.FcmMessageType;
import com.shakepoint.web.api.data.fcm.FcmNotification;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
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

    @Inject
    @ApplicationProperty(type = ApplicationProperty.Types.SYSTEM, name = "com.shakepoint.web.admin.orders.emails")
    private String adminEmails; //emails list, separated by commas

    private final Logger LOG = Logger.getLogger(getClass());
    private static final String RETRY_UPLOAD_QUEUE_NAME = "retry_file_upload";
    private static final String FCM_SEND_QUEUE_NAME = "fcm_send";

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
        List<ProductDTO> productsDTO = TransformationUtils.createProductsFromStatus(productsStatus);
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
            if (request.getPromoCode() != null && ! request.getPromoCode().isEmpty()) {
                //get it
                PromoCode promoCode = promoCodeRepository.findPromoCodeByCode(request.getPromoCode());
                if (promoCode == null) {
                    LOG.info("Invalid promo code attempt: " + request.getPromoCode());
                    //promo code is invalid
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(new PurchaseQRCode(null, false, "El código de promoción es inválido")).build();
                } else {
                    //check expiration date for promo code
                    if (promoCodeManager.isPromoCodeExpired(promoCode)) {
                        //expired
                        LOG.info("Expired promo code: " + promoCode.getCode() + "--" + promoCode.getExpirationDate());
                        return Response.status(Response.Status.BAD_REQUEST)
                                .entity(new PurchaseQRCode(null, false, "El código de promoción ha expirado")).build();
                    } else {
                        //check if user already redeemed promo code
                        if (promoCodeRepository.isPromoCodeAlreadyRedeemedByUser(promoCode.getCode(), user.getId())) {
                            LOG.info("Promo code already redeemed by user: " + promoCode.getCode());
                            return Response.status(Response.Status.BAD_REQUEST)
                                    .entity(new PurchaseQRCode(null, false, "El código de promoción ya ha sido canjeado anteriormente")).build();
                        } else {
                            double newTotal = calculatePurchaseTotal(purchase.getTotal(), promoCode.getDiscount());
                            LOG.info("Will set new total for purchase (before: " + purchase.getTotal() + ", now: " + newTotal + ")");
                            //set new price for purchase
                            purchase.setTotal(newTotal);
                            //call pay works to make payment
                            LOG.info("New price for purchase " + purchase.getProduct().getName() + " to " + purchase.getTotal());
                            LOG.info("Confirming purchase");
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
        if (purchase.getTotal() == 0) {
            //free purchase, no need to call api to make purchase
            return processSuccessfulDrink(purchase, user, null, promoCode);
        }
        //replace spaces on card number
        final String cardNumber = request.getCardNumber().replaceAll(" ", "");
        PaymentDetails paymentDetails = payWorksClientService.authorizePayment(cardNumber,
                request.getCardExpirationDate(), request.getCvv(), purchase.getTotal(), purchase.getControlNumber());
        if (paymentDetails == null) {
            LOG.info("No payment details from pay works");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new PurchaseQRCode(null, false, "Ha ocurrido un problema al realizar el pago, intenta nuevamente"))
                    .build();
        } else if (paymentDetails.getAuthCode() != null && paymentDetails.getPayworksResult().equals("A")) {
            return processSuccessfulDrink(purchase, user, paymentDetails, promoCode);
        } else if (paymentDetails.getPayworksResult().equals("D")) {
            //declined
            LOG.info("Purchase have been Rejected Declined");
            return Response.ok(new PurchaseQRCode(null, false, paymentDetails.getComputedMessage()))
                    .build();
        } else if (paymentDetails.getPayworksResult().equals("T")) {
            LOG.info("Timeout on provider");
            return Response.ok(new PurchaseQRCode(null, false, paymentDetails.getComputedMessage())).build();
        } else {
            LOG.info("Purchase have been Rejected");
            return Response.ok(new PurchaseQRCode(null, false, paymentDetails.getComputedMessage())).build();
        }
    }

    private Response processSuccessfulDrink(Purchase purchase, User user, PaymentDetails paymentDetails, PromoCode promoCode) {
        //payment went well
        purchase.setUser(user);
        purchase.setStatus(PurchaseStatus.AUTHORIZED);
        if (paymentDetails == null) {
            LOG.info("Will process purchase for free drink");
            purchase.setReference("TEST_REFERENCE");
        } else {
            purchase.setReference(paymentDetails.getReference());
        }
        purchaseRepository.update(purchase);
        if (user.getRole().equals(SecurityRole.MEMBER.getValue()) || user.getRole().equals(SecurityRole.TRAINER.getValue())) {
            //check how many purchases member/trainer has
            Integer purchasesCount = purchaseRepository.getUserPurchases(user.getId()).size();
            if (purchasesCount != 0 && purchasesCount % 10 == 0) {
                //multiple of 10, means it deserves another drink for 50%
                //create a new promo code for this unique user
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_YEAR, 7);
                PromoCode newUserPromoCode = promoCodeManager.createPromoCode(ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.format(calendar.getTime()),
                        "Te has ganado un descuento!", 50, PromoType.EARNED.getValue());
                LOG.info("Creating new promo code for free drink for 10 purchases");
                promoCodeRepository.createPromoCode(newUserPromoCode);
                Map<String, Object> earnedDiscountEmailParams = new HashMap<>();
                earnedDiscountEmailParams.put("username", user.getName());
                earnedDiscountEmailParams.put("promoCode", newUserPromoCode.getCode());
                earnedDiscountEmailParams.put("discount", newUserPromoCode.getDiscount());
                if (user.isEmailsEnabled()) {
                    emailSender.sendEmail(user.getEmail(), Template.EARNED_DRINK_DISCOUNT, earnedDiscountEmailParams);
                }
                if (user.isNotificationsEnabled()) {
                    jmsHandler.send(FCM_SEND_QUEUE_NAME, new Gson().toJson(FcmNotification.createNotification(FcmMessageType.TENTH_PURCHASE,
                            "Te has ganado un descuento!", "Felicidades!", user.getFcmToken(), earnedDiscountEmailParams)));
                }

            }
        }

        Map<String, Object> emailParams = new HashMap<String, Object>();
        emailParams.put("productName", purchase.getProduct().getName());
        emailParams.put("productPrice", purchase.getTotal());
        emailParams.put("purchaseDate", formatCompletePurchaseDate(purchase.getPurchaseDate()));
        if (promoCode != null) {
            emailParams.put("promoCode", promoCode.getCode());
            emailParams.put("promoDescription", promoCode.getDescription());
            //means user used promo code to pay this product
            //create promo code redemption
            PromoCodeRedeem redemption = promoCodeManager.createPromoCodeRedemption(promoCode, user);
            //add to database
            promoCodeRepository.redeemPromoCode(redemption);
            //send email
            if (user.isEmailsEnabled()) {
                emailSender.sendEmail(user.getEmail(), Template.SUCCESSFUL_PROMO_PURCHASE, emailParams);
            }

            //check if promo code contains a trainer assigned
            User trainer = promoCode.getTrainer();
            if (trainer != null) {
                //check number of redeems for promo code
                //TODO: GET ALL PROMO CODES THAT THIS USER HAVE REGISTERED WITH PURCHASES INSTEAD OF GETTING TOTAL NUMBER OF TIMES A SINGLE PROMO CODE HAVE BEEN REDEEMED.....
                //TODO: STUPID CODE GOES HERE..... DON'T FORGET IT
                Integer redemptionsCount = promoCodeRepository.getPromoCodeRedemptions(promoCode.getCode());
                if (redemptionsCount % 10 == 0) {
                    //deserves free drink
                    //creates a new promo code
                    LocalDate currentDate = LocalDate.now();
                    LocalDate expirationDate = currentDate.plus(1, ChronoUnit.MONTHS);
                    PromoCode earnedPromoCode = promoCodeManager.createPromoCode(expirationDate.format(DateTimeFormatter.ofPattern(ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.toPattern())),
                            "Te has ganado una bebida gratis!", 100, PromoType.EARNED.getValue());
                    promoCodeRepository.createPromoCode(earnedPromoCode);
                    LOG.info("Created new promo code for free drink for trainer");
                }
            }


            LOG.info("Successful purchase with promo code");
            if (paymentDetails != null) {
                return Response.ok(new PurchaseQRCode(purchase.getQrCodeUrl(), true, paymentDetails.getComputedMessage())).build();
            } else {
                return Response.ok(new PurchaseQRCode(purchase.getQrCodeUrl(), true, "Compra procesada con éxito")).build();
            }
        } else {
            //send standard email
            if (user.isEmailsEnabled()) {
                emailSender.sendEmail(user.getEmail(), Template.SUCCESSFUL_PURCHASE, emailParams);
            }

            LOG.info("Successful purchase");
            if (paymentDetails != null) {
                return Response.ok(new PurchaseQRCode(purchase.getQrCodeUrl(), true, paymentDetails.getComputedMessage())).build();
            } else {
                return Response.ok(new PurchaseQRCode(purchase.getQrCodeUrl(), true, "Compra procesada con éxito")).build();
            }
        }
    }

    private String formatCompletePurchaseDate(String purchaseDate) {
        try {
            Date date = ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.parse(purchaseDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            //get days
            int days = calendar.get(Calendar.DAY_OF_MONTH);
            int months = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            StringBuilder sb = new StringBuilder();
            sb.append(days);
            sb.append(" de ");
            switch (months) {
                case 0:
                    sb.append("Enero");
                    break;
                case 1:
                    sb.append("Febrero");
                    break;
                case 2:
                    sb.append("Marzo");
                    break;
                case 3:
                    sb.append("Abril");
                    break;
                case 4:
                    sb.append("Mayo");
                    break;
                case 5:
                    sb.append("Junio");
                    break;
                case 6:
                    sb.append("Julio");
                    break;
                case 7:
                    sb.append("Agosto");
                    break;
                case 8:
                    sb.append("Septiembre");
                    break;
                case 9:
                    sb.append("Octubre");
                    break;
                case 10:
                    sb.append("Noviembre");
                    break;
                case 11:
                    sb.append("Diciembre");
                    break;
            }
            sb.append(" del ");
            sb.append(year);

            return sb.toString();
        } catch (ParseException ex) {
            return null;
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
                profile = new UserProfileResponse
                        (user.getName(), user.getId(), user.getCreationDate(), false, null, 0.0, 0.0, 0.0, user.getEmail());
                userRepository.saveProfile(userProfile);
            } else {
                double purchasesTotal = TransformationUtils.getTotalPurchases(user.getPurchases());
                profile = TransformationUtils.createUserProfile(userProfile, purchasesTotal);
            }
        } catch (Exception ex) {
            LOG.error("Could not fetch user profile", ex);
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
    public Response getProductDetails(String productId, String vendingId, int slotNumber) {
        VendingMachineProductStatus existingProductStatus = machineRepository.getVendingProduct(productId, vendingId, slotNumber);
        //get number of scoops available for product type
        List<String> scoops = new ArrayList<String>();
        switch (existingProductStatus.getProduct().getType()) {
            case AMINO_ACID:
                scoops.addAll(ProductScoopsType.AMINOACID_SCOOPS);
                break;
            case OXIDE:
                scoops.addAll(ProductScoopsType.OXIDE_SCOOPS);
                break;
            case PROTEIN:
                scoops.addAll(ProductScoopsType.PROTEIN_SCOOPS);
                break;
        }
        ProductDTO dto = new ProductDTO(existingProductStatus.getProduct().getId(), existingProductStatus.getProduct().getName(),
                existingProductStatus.getProduct().getPrice(), existingProductStatus.getProduct().getDescription(),
                existingProductStatus.getProduct().getLogoUrl(),
                ProductType.getProductTypeForClient(existingProductStatus.getProduct().getType()), existingProductStatus.getProduct().getNutritionalDataUrl(), scoops);
        //get flavor
        ProductFlavorDTO flavorDTO = TransformationUtils.createFlavor(existingProductStatus.getFlavor());
        dto.setFlavor(flavorDTO);
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

    @Override
    public Response validatePromoCode(ValidatePromoCodeRequest request) {
        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        //get promo code
        PromoCode promoCode = promoCodeRepository.findPromoCodeByCode(request.getPromoCode());
        if (promoCode == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        PromoCodeValidation validation = promoCodeManager.validatePromoCode(request);
        return Response.ok(validation).build();
    }

    @Override
    public Response deactivateUser() {
        User user = userRepository.get(authenticatedUser.getId());
        user.setActive(false);
        userRepository.updateUser(user);
        return Response.ok().build();
    }

    @Override
    public Response activateUser() {
        User user = userRepository.get(authenticatedUser.getId());
        user.setActive(true);
        userRepository.updateUser(user);

        return Response.ok().build();
    }

    @Override
    public Response setNotificationsEnabled(boolean enabled) {
        //get user
        User user = userRepository.get(authenticatedUser.getId());
        user.setNotificationsEnabled(enabled);
        userRepository.updateUser(user);
        return Response.ok().build();
    }

    @Override
    public Response setEmailsEnabled(boolean enabled) {
        //get user
        User user = userRepository.get(authenticatedUser.getId());
        user.setEmailsEnabled(enabled);
        userRepository.updateUser(user);
        return Response.ok().build();
    }

    @Override
    public Response contact(ContactRequest request) {
        //get user
        User user = userRepository.get(authenticatedUser.getId());
        //send message
        Arrays.stream(adminEmails.split(",")).forEach(email -> {
            Map<String, Object> params = new HashMap();
            params.put("fromName", user.getName());
            params.put("fromEmail", user.getEmail());
            params.put("message", request.getMessage());
            emailSender.sendEmail(email, Template.CONTACT_EMAIL, params);
        });

        return Response.ok().build();

    }

    @Override
    public Response setFcmToken(FcmTokenRequest request) {
        userRepository.updateFcmToken(request.getFcmToken(), authenticatedUser.getId());
        return Response.ok().build();
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
