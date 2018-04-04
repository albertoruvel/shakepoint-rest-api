package com.shakepoint.web.api.data.dto.response.admin;

public class SimpleProduct {
    private String id;
    private String name;
    private String creationDate;
    private double price;
    private String logoUrl;

    public SimpleProduct() {
    }

    public SimpleProduct(String id, String name, String creationDate, double price, String logoUrl) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.price = price;
        this.logoUrl = logoUrl;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLogoUrl() {
        return logoUrl;
    }


    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
