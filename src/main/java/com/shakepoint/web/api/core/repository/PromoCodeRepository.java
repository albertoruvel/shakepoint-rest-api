package com.shakepoint.web.api.core.repository;

import com.shakepoint.web.api.data.entity.PromoCode;

import java.util.List;

public interface PromoCodeRepository {
    public void createPromoCode(PromoCode promoCode);

    public List<PromoCode> getAllPromoCodes();
    public PromoCode findPromoCodeByCode(String code);
}
