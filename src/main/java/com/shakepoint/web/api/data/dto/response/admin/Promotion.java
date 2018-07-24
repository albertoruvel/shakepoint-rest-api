package com.shakepoint.web.api.data.dto.response.admin;

import com.shakepoint.web.api.data.dto.response.ProductDTO;

public class Promotion {
    private String id;
    private String expirationDate;
    private SimpleProduct product;
    private int discount;
    private String type;
    private String code;

    public Promotion(String id, String expirationDate, SimpleProduct product, int discount, String type, String code) {
        this.id = id;
        this.expirationDate = expirationDate;
        this.product = product;
        this.discount = discount;
        this.type = type;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public SimpleProduct getProduct() {
        return product;
    }

    public void setProduct(SimpleProduct product) {
        this.product = product;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
