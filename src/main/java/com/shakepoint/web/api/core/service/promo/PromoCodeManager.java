package com.shakepoint.web.api.core.service.promo;

import com.shakepoint.web.api.data.dto.request.ValidatePromoCodeRequest;
import com.shakepoint.web.api.data.dto.response.PromoCodeValidation;
import com.shakepoint.web.api.data.entity.PromoCode;
import com.shakepoint.web.api.data.entity.PromoCodeRedeem;
import com.shakepoint.web.api.data.entity.PromoType;
import com.shakepoint.web.api.data.entity.User;

public interface PromoCodeManager {

    public boolean isPromoCodeExpired(PromoCode promoCode);
    public PromoCode createPromoCode(String expirationDate, String description, int discount, int promotionType);

    public PromoCodeRedeem createPromoCodeRedemption(PromoCode promoCode, User user);

    public PromoCodeValidation validatePromoCode(ValidatePromoCodeRequest request);
}
