package com.shakepoint.web.api.data.dto.request;

public class ValidatePromoCodeRequest {
    private String promoCode;
    private String productId;

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
