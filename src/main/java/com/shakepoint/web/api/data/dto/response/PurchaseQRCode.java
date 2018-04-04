package com.shakepoint.web.api.data.dto.response;

public class PurchaseQRCode {
    private String imageUrl;
    private boolean success;
    private String message;

    public PurchaseQRCode() {
    }

    public PurchaseQRCode(String imageUrl, boolean success, String message) {
        this.imageUrl = imageUrl;
        this.success = success;
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
