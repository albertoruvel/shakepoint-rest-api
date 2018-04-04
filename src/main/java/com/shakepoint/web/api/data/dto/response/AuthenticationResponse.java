package com.shakepoint.web.api.data.dto.response;

public class AuthenticationResponse {
    private String message;
    private String authenticationToken;
    private boolean success;

    public AuthenticationResponse(String message, String authenticationToken, boolean success) {
        this.message = message;
        this.authenticationToken = authenticationToken;
        this.success = success;
    }

    public AuthenticationResponse(String message){
        this.message = message;
        this.success = false;
    }

    public AuthenticationResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
