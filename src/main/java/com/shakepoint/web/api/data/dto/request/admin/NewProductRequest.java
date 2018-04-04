package com.shakepoint.web.api.data.dto.request.admin;

public class NewProductRequest {
    private String name;
    private double price;
    private String description;
    private String logoUrl;
    private String engineUseTime;
    private String productType;
    private byte[] nutritionalDataImage;

    public NewProductRequest(String name, double price, String description, String logoUrl, String engineUseTime, String productType, byte[] nutritionalDataImage) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.logoUrl = logoUrl;
        this.engineUseTime = engineUseTime;
        this.productType = productType;
        this.nutritionalDataImage = nutritionalDataImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getEngineUseTime() {
        return engineUseTime;
    }

    public void setEngineUseTime(String engineUseTime) {
        this.engineUseTime = engineUseTime;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public byte[] getNutritionalDataImage() {
        return nutritionalDataImage;
    }

    public void setNutritionalDataImage(byte[] nutritionalDataImage) {
        this.nutritionalDataImage = nutritionalDataImage;
    }
}
