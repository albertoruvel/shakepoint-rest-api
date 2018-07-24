package com.shakepoint.web.api.core.service.promo;

import com.shakepoint.web.api.core.util.ShakeUtils;
import com.shakepoint.web.api.data.entity.PromoCode;
import com.shakepoint.web.api.data.entity.PromoType;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class PromoCodeManagerImpl implements PromoCodeManager {
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
    public PromoCode createPromoCode(String expirationDate, String description, int discount, PromoType promoType) {
        //create a new code
        String promoCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        PromoCode promoCodeEntity = new PromoCode();
        promoCodeEntity.setCode(promoCode);
        promoCodeEntity.setExpirationDate(expirationDate);
        promoCodeEntity.setDescription(description);
        promoCodeEntity.setDiscount(discount);
        promoCodeEntity.setType(promoType);
        return promoCodeEntity;
    }
}
