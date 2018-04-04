package com.shakepoint.web.api.data.dto.response.admin;

public class UserDescription {
    private String id;
    private String name;
    private String email;
    private double purchasesTotal;

    public UserDescription(String id, String name, String email, double purchasesTotal) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.purchasesTotal = purchasesTotal;
    }

    public UserDescription() {
        super();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getPurchasesTotal() {
        return purchasesTotal;
    }

    public void setPurchasesTotal(double purchasesTotal) {
        this.purchasesTotal = purchasesTotal;
    }


}
