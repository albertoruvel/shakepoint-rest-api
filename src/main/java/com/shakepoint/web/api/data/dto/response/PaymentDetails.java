package com.shakepoint.web.api.data.dto.response;

public class PaymentDetails {
    private String authCode;
    private String requestDate;
    private String responseDate;
    private String merchantId;
    private String reference;
    private String payworksResult;
    private String message;
    private String computedMessage;

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

    public String getComputedMessage() {
        if (payworksResult.equals("A")) {
            return "Compra realizada con éxito";
        } else if (payworksResult.equals("D")) {
            return "La tarjeta proporcionada ha sido declinada";
        } else if (payworksResult.equals("T")) {
            return "No se ha obtenido respuesta del autorizador, revisa los datos e intenta nuevamente";
        } else return "La tarjeta proporcionada ha sido rechazada";
    }
}
