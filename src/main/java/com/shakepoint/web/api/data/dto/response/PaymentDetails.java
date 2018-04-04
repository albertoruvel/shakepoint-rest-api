package com.shakepoint.web.api.data.dto.response;

public class PaymentDetails {
    private String authCode;
    private String requestDate;
    private String responseDate;
    private String merchantId;
    private String reference;
    private String payworksResult;
    private String message;

    public PaymentDetails(String authCode, String requestDate, String responseDate, String merchantId, String reference, String payworksResult, String message) {
        this.authCode = authCode;
        this.requestDate = requestDate;
        this.responseDate = responseDate;
        this.merchantId = merchantId;
        this.reference = reference;
        this.payworksResult = payworksResult;
        this.message = message;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getResponseDate() {
        return responseDate;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getReference() {
        return reference;
    }

    public String getPayworksResult() {
        return payworksResult;
    }

    public String getMessage() {
        return message;
    }
}
