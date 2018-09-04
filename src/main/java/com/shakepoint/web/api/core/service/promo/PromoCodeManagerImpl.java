package com.shakepoint.web.api.core.service.promo;

import com.shakepoint.web.api.core.repository.ProductRepository;
import com.shakepoint.web.api.core.repository.PromoCodeRepository;
import com.shakepoint.web.api.core.service.security.AuthenticatedUser;
import com.shakepoint.web.api.core.service.security.RequestPrincipal;
import com.shakepoint.web.api.core.util.ShakeUtils;
import com.shakepoint.web.api.data.dto.request.ValidatePromoCodeRequest;
import com.shakepoint.web.api.data.dto.response.PromoCodeValidation;
import com.shakepoint.web.api.data.entity.Product;
import com.shakepoint.web.api.data.entity.PromoCode;
import com.shakepoint.web.api.data.entity.PromoCodeRedeem;
import com.shakepoint.web.api.data.entity.PromoType;
import com.shakepoint.web.api.data.entity.User;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class PromoCodeManagerImpl implements PromoCodeManager {

    @Inject
    private PromoCodeRepository promoCodeRepository;

    @Inject
    @AuthenticatedUser
    private RequestPrincipal authenticatedUser;

    @Inject
    private ProductRepository productRepository;

    @Override
    public boolean isPromoCodeExpired(PromoCode promoCode){
        try{
            //check expiration date
            Date expirationDate = ShakeUtils.SIMPLE_DATE_FORMAT.parse(promoCode.getExpirationDate());
            //create current date
            Calendar calendar = Calendar.getInstance();
            //get difference between dates
            long expirationDateTime = expirationDate.getTime();
            long currentDateTime = calendar.getTime().getTime();

            if (expirationDateTime <=  currentDateTime){
                //expired
                return false;
            }
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    @Override
    public PromoCode createPromoCode(String expirationDate, String description, int discount, int promotionType) {
        //create a new code
        String promoCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        PromoCode promoCodeEntity = new PromoCode();
        promoCodeEntity.setCode(promoCode);
        promoCodeEntity.setExpirationDate(expirationDate);
        promoCodeEntity.setDescription(description);
        promoCodeEntity.setDiscount(discount);
        promoCodeEntity.setType(promotionType);
        return promoCodeEntity;
    }

    @Override
    public PromoCodeRedeem createPromoCodeRedemption(PromoCode promoCode, User user) {
        PromoCodeRedeem redeem = new PromoCodeRedeem();
        redeem.setPromoCode(promoCode);
        redeem.setUser(user);
        redeem.setRedemptionDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        return redeem;
    }

    @Override
    public PromoCodeValidation validatePromoCode(ValidatePromoCodeRequest request) {
        PromoCode promoCode = promoCodeRepository.findPromoCodeByCode(request.getPromoCode());
        if (promoCode == null) {
            return new PromoCodeValidation("El código es inválido", false, -1D, 0D);
        } else if (! promoCode.getCode().equals(request.getPromoCode())) {
            return new PromoCodeValidation("El código no es válido", false, -1D, 0D);
        } else if (isPromoCodeExpired(promoCode)) {
            return new PromoCodeValidation("El código ha expirado", false, -1D, 0D);
        }

        //code is correct
        //check promo code type and if have been redeemed before
        PromoType promoType = PromoType.fromValue(promoCode.getType());
        switch (promoType) {
            case OPEN:
                //open for single product
                //validate purchasing product with promo code product
                if (promoCode.getProduct().getId().equals(request.getProductId())) {
                    //promo code looks valid
                    return createValidation("Success", true, promoCode, request.getProductId());
                } else {
                    // promo code for invalid product
                    return new PromoCodeValidation("El código no es válido para este producto", false, -1D, 0D);
                }
            case OPEN_ALL:
                //promo code is valid for any product
                return createValidation("Success", true, promoCode, request.getProductId());
            case TRAINER:
                //promo code is valid for any product
                return createValidation("Success", true, promoCode, request.getProductId());
            case BIRTHDATE:
                return createValidation("Success", true, promoCode, request.getProductId());
            case EARNED:
                return createValidation("Success", true, promoCode, request.getProductId());
            case SEASON:
                if (promoCode.getProduct().getId().equals(request.getProductId())) {
                    return createValidation("Success", true, promoCode, request.getProductId());
                } else {
                    return new PromoCodeValidation("El código no es válido para este producto", false, -1D, 0D);
                }
            case SEASON_ALL:
                return createValidation("Success", true, promoCode, request.getProductId());

                default:
                    return new PromoCodeValidation("El código es inválido", false, -1D, 0D);
        }
    }

    private PromoCodeValidation createValidation(String message, boolean valid, PromoCode promoCode, String productId) {
        Product product = productRepository.getProduct(productId);
        PromoCodeValidation validation = new PromoCodeValidation();
        validation.setDiscount(getDiscount(promoCode, product));
        validation.setMessage(message);
        validation.setValid(valid);
        //calculate new price
        PromoType type = PromoType.fromValue(promoCode.getType());
        switch(type){
            case BIRTHDATE:
                //any product
                validation.setNewPrice(0D);
                break;
            case EARNED:
                validation.setNewPrice(product.getPrice() - validation.getDiscount());
                break;
            case OPEN:
                validation.setNewPrice(product.getPrice() - validation.getDiscount());
                break;
            case OPEN_ALL:
                validation.setNewPrice(product.getPrice() - validation.getDiscount());
                break;
            case SEASON:
                validation.setNewPrice(product.getPrice() - validation.getDiscount());
                break;
            case SEASON_ALL:
                validation.setNewPrice(product.getPrice() - validation.getDiscount());
                break;
            case TRAINER:
                validation.setNewPrice(product.getPrice() - validation.getDiscount());
                break;
        }
        return validation;
    }

    private Double getDiscount(PromoCode promoCode, Product product) {
        if (promoCode.getDiscount() == 100) {
            //free drink!! :D
            return product.getPrice();
        } else {
            double discDec = (Double.valueOf(promoCode.getDiscount()) / 100d);
            double discValue = product.getPrice() * discDec;
            return discValue;
        }
    }
}
