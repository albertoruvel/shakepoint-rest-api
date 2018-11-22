package com.shakepoint.web.api.core.util;

import com.shakepoint.web.api.core.machine.ProductType;
import com.shakepoint.web.api.core.repository.MachineRepository;
import com.shakepoint.web.api.core.service.security.CryptoService;
import com.shakepoint.web.api.core.service.security.SecurityRole;
import com.shakepoint.web.api.data.dto.request.SignupRequest;
import com.shakepoint.web.api.data.dto.request.UserProfileRequest;
import com.shakepoint.web.api.data.dto.request.admin.NewMachineRequest;
import com.shakepoint.web.api.data.dto.request.admin.NewProductRequest;
import com.shakepoint.web.api.data.dto.request.admin.NewTechnicianRequest;
import com.shakepoint.web.api.data.dto.request.partner.CreateTrainerRequest;
import com.shakepoint.web.api.data.dto.response.ProductDTO;
import com.shakepoint.web.api.data.dto.response.ProductFlavorDTO;
import com.shakepoint.web.api.data.dto.response.PurchaseCodeResponse;
import com.shakepoint.web.api.data.dto.response.SimpleMachineProduct;
import com.shakepoint.web.api.data.dto.response.UserProfileResponse;
import com.shakepoint.web.api.data.dto.response.UserPurchaseResponse;
import com.shakepoint.web.api.data.dto.response.admin.ProductLevelDescription;
import com.shakepoint.web.api.data.dto.response.admin.Promotion;
import com.shakepoint.web.api.data.dto.response.admin.SimpleMachine;
import com.shakepoint.web.api.data.dto.response.admin.SimpleProduct;
import com.shakepoint.web.api.data.dto.response.admin.Technician;
import com.shakepoint.web.api.data.dto.response.admin.TechnicianMachine;
import com.shakepoint.web.api.data.dto.response.admin.VendingProductDetails;
import com.shakepoint.web.api.data.dto.response.partner.Trainer;
import com.shakepoint.web.api.data.entity.Flavor;
import com.shakepoint.web.api.data.entity.Product;
import com.shakepoint.web.api.data.entity.PromoCode;
import com.shakepoint.web.api.data.entity.PromoType;
import com.shakepoint.web.api.data.entity.Purchase;
import com.shakepoint.web.api.data.entity.TrainerInformation;
import com.shakepoint.web.api.data.entity.User;
import com.shakepoint.web.api.data.entity.UserProfile;
import com.shakepoint.web.api.data.entity.VendingConnection;
import com.shakepoint.web.api.data.entity.VendingMachine;
import com.shakepoint.web.api.data.entity.VendingMachineProductStatus;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransformationUtils {

    private static final Logger log = Logger.getLogger(TransformationUtils.class);

    public static Product createProductFromDto(NewProductRequest product) {
        Product p = new Product();
        try {
            p.setName(product.getName());
            p.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
            p.setDescription(new String(product.getDescription().getBytes(), "UTF-8"));
            p.setLogoUrl(product.getLogoUrl());
            p.setPrice(product.getPrice());
            p.setType(ProductType.getProductType(product.getProductType()));
            p.setEngineUseTime(product.getEngineUseTime());
            p.setMixTime(product.getMixTime());
        } catch (UnsupportedEncodingException ex) {
            log.info("Could not transform encoding", ex);
        }
        return p;
    }

    public static User getUser(SignupRequest request, String encryptedPassword) {
        User user = new User();
        user.setActive(true);
        user.setConfirmed(false);
        user.setAccessToken(ShakeUtils.getNextSessionToken());
        user.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(encryptedPassword);
        user.setRole(SecurityRole.MEMBER.toString());
        return user;
    }

    public static User getUser(com.shakepoint.web.api.data.dto.request.admin.CreateTrainerRequest request, String encryptedPassword) {
        User user = new User();
        user.setActive(true);
        user.setConfirmed(false);
        user.setAccessToken(ShakeUtils.getNextSessionToken());
        user.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(encryptedPassword);
        user.setRole(SecurityRole.TRAINER.toString());
        user.setEmailsEnabled(true);
        user.setNotificationsEnabled(true);
        return user;
    }

    public static List<Technician> createTechnicianDtoList(List<User> techs) {
        List<Technician> list = new ArrayList();
        Technician dto;
        for (User user : techs) {
            dto = new Technician();
            dto.setCreationDate(user.getCreationDate());
            dto.setName(user.getName());
            dto.setActive(user.isActive());
            dto.setEmail(user.getEmail());
            dto.setId(user.getId());
            list.add(dto);
        }
        return list;
    }

    public static VendingMachine getMachineFromDTO(NewMachineRequest dto) {
        VendingMachine machine = new VendingMachine();
        machine.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        machine.setDescription(dto.getDescription());
        machine.setName(dto.getName());

        machine.setLocation(dto.getLocation());
        machine.setActive(false);
        machine.setError(false);
        machine.setSlots(6);
        return machine;
    }

    public static UserProfile getProfile(String userId, UserProfileRequest request) {
        UserProfile profile = new UserProfile();
        profile.setHeight(request.getHeight());
        profile.setWeight(request.getWeight());
        return profile;
    }

    public static Technician createTechnician(User dto) {
        return new Technician(dto.getId(), dto.getName(), dto.getEmail(), dto.getCreationDate(), dto.isActive());
    }

    public static List<SimpleProduct> createSimpleProducts(List<Product> page) {
        List<SimpleProduct> products = new ArrayList();
        for (Product p : page) {
            products.add(createSimpleProduct(p));
        }
        return products;
    }

    public static List<VendingProductDetails> createVendingProductDetailsFromStatus(List<VendingMachineProductStatus> products) {
        List<VendingProductDetails> list = new ArrayList();
        for (VendingMachineProductStatus status : products) {
            list.add(createVendingProductDetails(status));
        }
        return list;
    }

    private static VendingProductDetails createVendingProductDetails(VendingMachineProductStatus status) {
        return new VendingProductDetails(status.getId(), status.getProduct().getId(), status.getProduct().getLogoUrl(),
                status.getSlotNumber(), status.getPercentage(), status.getProduct().getName());
    }

    public static List<SimpleProduct> createSimpleProductsFromStatus(List<VendingMachineProductStatus> products) {
        List<SimpleProduct> list = new ArrayList();
        for (VendingMachineProductStatus status : products) {
            list.add(createSimpleProduct(status.getProduct()));
        }
        return list;
    }

    public static List<SimpleMachine> createSimpleMachines(List<VendingMachine> page, MachineRepository machineRepository) {
        List<SimpleMachine> machines = new ArrayList();
        VendingConnection conn = null;
        for (VendingMachine m : page) {
            conn = machineRepository.getVendingConnection(m.getId());
            machines.add(new SimpleMachine(m.getId(), m.getName(), m.getDescription(), m.getTechnician().getName(),
                    conn.getPort(), conn.getActive()));
        }

        return machines;
    }

    public static User getUserFromTechnician(NewTechnicianRequest dto, final String encryptedPassword) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(encryptedPassword);
        user.setAccessToken(ShakeUtils.getNextSessionToken());
        user.setConfirmed(false);
        user.setActive(true);
        user.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        user.setAddedBy(null);
        user.setRole(SecurityRole.PARTNER.toString());
        return user;
    }

    public static SimpleMachineProduct createSimpleMachineProduct(VendingMachineProductStatus machineProduct) {
        return new SimpleMachineProduct(machineProduct.getId(), machineProduct.getProduct().getName(),
                machineProduct.getProduct().getLogoUrl(),
                machineProduct.getSlotNumber());
    }

    public static List<SimpleMachineProduct> createSimpleMachineProducts(List<VendingMachineProductStatus> s) {
        List<SimpleMachineProduct> list = new ArrayList();
        for (VendingMachineProductStatus status : s) {
            list.add(createSimpleMachineProduct(status));
        }

        return list;
    }

    public static SimpleProduct createSimpleProduct(Product p) {
        return new SimpleProduct(p.getId(), p.getName(), p.getCreationDate(), p.getPrice(), p.getLogoUrl(), p.getDescription());
    }

    public static List<PurchaseCodeResponse> createPurchaseCodes(List<Purchase> activeCodes) {
        List<PurchaseCodeResponse> codes = new ArrayList();
        for (Purchase code : activeCodes) {
            codes.add(new PurchaseCodeResponse(code.getQrCodeUrl(), code.getProduct().getName(), code.getMachine().getName(), code.getPurchaseDate()));
        }
        return codes;
    }

    public static UserProfileResponse createUserProfile(UserProfile userProfile, double totalPurchases) {
        UserProfileResponse response = new UserProfileResponse(userProfile.getUser().getName(), userProfile.getUser().getId(),
                userProfile.getUser().getCreationDate(), true, userProfile.getBirthday(), userProfile.getWeight(),
                userProfile.getHeight(), totalPurchases, userProfile.getUser().getEmail());
        return response;
    }

    public static double getTotalPurchases(List<Purchase> purchases) {
        double total = 0;
        for (Purchase p : purchases) {
            total += p.getTotal();
        }
        return total;
    }

    public static List<TechnicianMachine> createTechnicianMachinesList(List<VendingMachine> machines, MachineRepository instance) {
        List<TechnicianMachine> dtos = new ArrayList();
        for (VendingMachine machine : machines) {
            dtos.add(new TechnicianMachine(machine.getId(), machine.getName(), machine.getDescription(), instance.isMachineAlerted(machine.getId()),
                    instance.getMachineProducts(machine.getId()).size(), 6));
        }
        return dtos;
    }

    public static List<ProductDTO> createProducts(List<Product> entities) {
        List<ProductDTO> productsList = new ArrayList();
        try {
            for (Product p : entities) {
                productsList.add(new ProductDTO(p.getId(), p.getName(), p.getPrice(), new String(p.getDescription().getBytes(), "UTF-8"), p.getLogoUrl(),
                        ProductType.getProductTypeForClient(p.getType()), p.getNutritionalDataUrl()));
            }
        } catch (UnsupportedEncodingException ex) {
            log.info("Could not transform encoding", ex);
        }
        return productsList;
    }

    public static List<UserPurchaseResponse> createPurchases(List<Purchase> purchases) {
        List<UserPurchaseResponse> ps = new ArrayList();

        for (Purchase p : purchases) {
            ps.add(new UserPurchaseResponse(p.getId(), p.getTotal(), p.getProduct().getName(), p.getMachine().getName(), p.getPurchaseDate()));
        }
        return ps;
    }

    public static List<ProductLevelDescription> createProductLevelDescriptions(List<VendingMachineProductStatus> products) {
        List<ProductLevelDescription> descriptions = new ArrayList();

        for (VendingMachineProductStatus status : products) {
            descriptions.add(createProductLevelDescription(status));
        }
        return descriptions;
    }

    public static ProductLevelDescription createProductLevelDescription(VendingMachineProductStatus status) {
        return new ProductLevelDescription(
                status.getPercentage() < 30 ? true : false,
                status.getProduct().getId(),
                status.getProduct().getName(),
                0,
                status.getProduct().getLogoUrl()
        );
    }

    public static NewProductRequest createProductRequestFromMultipart(MultipartFormDataInput multipart) {
        try (InputStream is = multipart.getFormDataPart("file", InputStream.class, null)) {
            byte[] bytes = IOUtils.toByteArray(is);
            final String name = multipart.getFormDataPart("name", String.class, null);
            final Double price = multipart.getFormDataPart("price", Double.class, null);
            final String description = multipart.getFormDataPart("description", String.class, null);
            final String logoUrl = multipart.getFormDataPart("logoUrl", String.class, null);
            final String engineUseTime = multipart.getFormDataPart("engineUseTime", String.class, null);
            final String productType = multipart.getFormDataPart("productType", String.class, null);
            final String mixTime = multipart.getFormDataPart("mixTime", String.class, null);
            return new NewProductRequest(name, price, description, logoUrl, engineUseTime, productType, Integer.parseInt(mixTime), bytes);
        } catch (IOException ex) {
            log.error("Could not extract data from multipart", ex);
            return null;
        }
    }

    public static Promotion createPromoCode(PromoCode promo, Product product, Trainer trainer) {
        if (product != null) {
            return new Promotion(promo.getId(), promo.getExpirationDate(), createSimpleProduct(product), promo.getDiscount(),
                    PromoType.fromValue(promo.getType()).toString(), promo.getCode(), trainer, promo.getDescription());
        } else {
            return new Promotion(promo.getId(), promo.getExpirationDate(), null, promo.getDiscount(),
                    PromoType.fromValue(promo.getType()).toString(), promo.getCode(), trainer, promo.getDescription());
        }
    }

    public static List<Promotion> createPromoCodes(List<PromoCode> promoCodes) {
        final List<Promotion> promotions = new ArrayList();
        promoCodes.stream().forEach(promotion -> {
            Promotion newPromotion = new Promotion();
            newPromotion.setId(promotion.getId());
            newPromotion.setCode(promotion.getCode());
            newPromotion.setDiscount(promotion.getDiscount());
            newPromotion.setExpirationDate(promotion.getExpirationDate());
            newPromotion.setType(PromoType.fromValue(promotion.getType()).toString());
            newPromotion.setDescription(promotion.getDescription());
            SimpleProduct simpleProduct = null;
            if (promotion.getProduct() != null) {
                //add product details
                simpleProduct = createSimpleProduct(promotion.getProduct());
                newPromotion.setProduct(simpleProduct);
            }
            if (promotion.getTrainer() != null) {
                newPromotion.setTrainer(new com.shakepoint.web.api.data.dto.response.partner.Trainer(
                        promotion.getTrainer().getId(), promotion.getTrainer().getName(), promotion.getTrainer().getEmail()
                ));
            }
            promotions.add(newPromotion);
        });
        return promotions;
    }

    public static User createUserFromTrainerRequest(CreateTrainerRequest request, String newToken, CryptoService cryptoService) {
        User user = new User();
        user.setPassword(cryptoService.encrypt(request.getPassword()));
        user.setAccessToken(newToken);
        user.setActive(true);
        user.setConfirmed(false);
        user.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setRole(SecurityRole.TRAINER.toString());
        return user;
    }

    public static List<com.shakepoint.web.api.data.dto.response.partner.Trainer> createTrainers(List<User> trainers) {
        List<com.shakepoint.web.api.data.dto.response.partner.Trainer> trainersList = new ArrayList<>();
        trainers.stream().forEach(t -> {
            trainersList.add(createTrainer(t.getId(), t.getName(), t.getEmail()));
        });
        return trainersList;
    }

    public static com.shakepoint.web.api.data.dto.response.partner.Trainer createTrainer(String id, String name, String email) {
        return new com.shakepoint.web.api.data.dto.response.partner.Trainer(id, name, email);
    }

    public static TrainerInformation createTrainerInformation(User trainer, User partner) {
        TrainerInformation trainerInformation = new TrainerInformation();
        trainerInformation.setPartner(partner.getId());
        trainerInformation.setTrainerUser(trainer.getId());
        trainerInformation.setRegistrationDate(ShakeUtils.SIMPLE_DATE_FORMAT.format(new Date()));
        return trainerInformation;
    }

    public static ProductFlavorDTO createFlavor(Flavor flavor) {
        return new ProductFlavorDTO(flavor.getId(), flavor.getHexColor(), flavor.getName());
    }
}
