package com.shakepoint.web.api.data.dto.response;

public class ValidateForgotPasswordTokenResponse {
    private Boolean isTokenValid;

    public ValidateForgotPasswordTokenResponse(Boolean isTokenValid) {
        this.isTokenValid = isTokenValid;
    }

    public Boolean getTokenValid() {
        return isTokenValid;
    }

    public void setTokenValid(Boolean tokenValid) {
        isTokenValid = tokenValid;
    }
}
