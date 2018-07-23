package com.shakepoint.web.api.core.service.promo;

import com.shakepoint.web.api.data.entity.PromoCode;
import com.shakepoint.web.api.data.entity.PromoType;

import java.util.UUID;

public class PromoCodeManagerImpl implements PromoCodeManager {
    @Override
    public PromoCode createOpenPromoCode(String expirationDate, String description, int discount) {
        //create a new code
        String promoCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        PromoCode promoCodeEntity = new PromoCode();
        promoCodeEntity.setCode(promoCode);
        promoCodeEntity.setExpirationDate(expirationDate);
        promoCodeEntity.setDescription(description);
        promoCodeEntity.setDiscount(discount);
        promoCodeEntity.setType(PromoType.OPEN);
        return promoCodeEntity;
    }
}
