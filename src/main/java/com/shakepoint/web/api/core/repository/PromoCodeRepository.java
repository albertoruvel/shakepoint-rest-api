package com.shakepoint.web.api.core.repository;

import com.shakepoint.web.api.data.entity.PromoCode;
import com.shakepoint.web.api.data.entity.PromoCodeRedeem;
import com.shakepoint.web.api.data.entity.User;

import java.util.List;

public interface PromoCodeRepository {
    public void createPromoCode(PromoCode promoCode);

    public List<PromoCode> getAllPromoCodes();

    public PromoCode findPromoCodeByCode(String code);

    public void redeemPromoCode(PromoCodeRedeem redemption);

    public boolean isPromoCodeAlreadyRedeemedByUser(String code, String id);


    public Integer getPromoCodeRedemptions(String code);

    public Integer getCountForTrainerPromoCode(String code);
}
