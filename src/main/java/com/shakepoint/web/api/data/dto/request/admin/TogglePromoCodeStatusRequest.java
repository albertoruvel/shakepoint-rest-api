package com.shakepoint.web.api.data.dto.request.admin;

public class TogglePromoCodeStatusRequest {
    private boolean active;
    private String promoCodeId;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(String promoCodeId) {
        this.promoCodeId = promoCodeId;
    }
}
