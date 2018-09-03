package com.shakepoint.web.api.data.dto.response.admin;

import com.shakepoint.web.api.data.dto.response.partner.Trainer;

public class Promotion {
    private String id;
    private String expirationDate;
    private SimpleProduct product;
    private int discount;
    private String type;
    private String code;
    private Trainer trainer;

    public Promotion(String id, String expirationDate, SimpleProduct product, int discount, String type, String code, Trainer trainer) {
        this.id = id;
        this.expirationDate = expirationDate;
        this.product = product;
        this.discount = discount;
        this.type = type;
        this.code = code;
        this.trainer = trainer;
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

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }
}
