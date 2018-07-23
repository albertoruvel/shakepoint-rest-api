package com.shakepoint.web.api.core.service.promo;

import com.shakepoint.web.api.data.entity.PromoCode;

public interface PromoCodeManager {

    public PromoCode createOpenPromoCode(String expirationDate, String description, int discount);
}
