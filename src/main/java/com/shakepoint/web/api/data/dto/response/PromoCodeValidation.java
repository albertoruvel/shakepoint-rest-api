package com.shakepoint.web.api.data.dto.response;

public class PromoCodeValidation {
    private String message;
    private boolean valid;
    private Double newPrice;
    private Double discount;

    public PromoCodeValidation(String message, boolean valid, Double newPrice, Double discount) {
        this.message = message;
        this.valid = valid;
        this.newPrice = newPrice;
        this.discount = discount;
    }

    public PromoCodeValidation() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(Double newPrice) {
        this.newPrice = newPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
