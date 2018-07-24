package com.shakepoint.web.api.core.service.promo;

import com.shakepoint.web.api.data.entity.PromoCode;
import com.shakepoint.web.api.data.entity.PromoType;

public interface PromoCodeManager {

    public boolean isPromoCodeExpired(PromoCode promoCode);
    public PromoCode createPromoCode(String expirationDate, String description, int discount, PromoType promoType);
}
