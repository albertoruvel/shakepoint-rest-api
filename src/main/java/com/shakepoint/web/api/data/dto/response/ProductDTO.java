package com.shakepoint.web.api.data.dto.response;

import java.util.List;

public class ProductDTO {
    private String id;
    private String name;
    private double price;
    private String description;
    private String logoUrl;
    private String productType;
    private String nutritionalDataUrl;
    private List<String> availableScoops;

    public ProductDTO(String id, String name, double price, String description, String logoUrl, String productType, String nutriData) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.logoUrl = logoUrl;
        this.productType = productType;
        this.nutritionalDataUrl = nutriData;
    }

    public ProductDTO(String id, String name, double price, String description, String logoUrl, String productType, String nutritionalDataUrl, List<String> availableScoops) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.logoUrl = logoUrl;
        this.productType = productType;
        this.nutritionalDataUrl = nutritionalDataUrl;
        this.availableScoops = availableScoops;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getNutritionalDataUrl() {
        return nutritionalDataUrl;
    }

    public void setNutritionalDataUrl(String nutritionalDataUrl) {
        this.nutritionalDataUrl = nutritionalDataUrl;
    }

    public List<String> getAvailableScoops() {
        return availableScoops;
    }

    public void setAvailableScoops(List<String> availableScoops) {
        this.availableScoops = availableScoops;
    }
}
