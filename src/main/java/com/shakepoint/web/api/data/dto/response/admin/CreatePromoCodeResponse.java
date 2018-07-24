package com.shakepoint.web.api.data.dto.response.admin;

public class CreatePromoCodeResponse {
    private String message;

    public CreatePromoCodeResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
