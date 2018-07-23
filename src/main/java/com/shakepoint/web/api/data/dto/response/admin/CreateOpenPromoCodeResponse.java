package com.shakepoint.web.api.data.dto.response.admin;

public class CreateOpenPromoCodeResponse {
    private String message;

    public CreateOpenPromoCodeResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
