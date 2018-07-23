package com.shakepoint.web.api.data.dto.response.admin;

public class VendingProductResponse {
    private String vendingProductId;

    public VendingProductResponse(String vendingProductId) {
        this.vendingProductId = vendingProductId;
    }

    public String getVendingProductId() {
        return vendingProductId;
    }

    public void setVendingProductId(String vendingProductId) {
        this.vendingProductId = vendingProductId;
    }
}
