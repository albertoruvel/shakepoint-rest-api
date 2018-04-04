package com.shakepoint.web.api.data.dto.response;

public class SimpleMachineProduct {
    private String id;
    private String productName;
    private String productLogoUrl;
    private int slotNumber;

    public SimpleMachineProduct(String id, String productName, String productLogoUrl, int slotNumber) {
        this.id = id;
        this.productName = productName;
        this.productLogoUrl = productLogoUrl;
        this.slotNumber = slotNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductLogoUrl() {
        return productLogoUrl;
    }

    public void setProductLogoUrl(String productLogoUrl) {
        this.productLogoUrl = productLogoUrl;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }
}
