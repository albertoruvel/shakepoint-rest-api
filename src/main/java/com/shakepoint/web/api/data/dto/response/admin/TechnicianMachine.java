package com.shakepoint.web.api.data.dto.response.admin;

public class TechnicianMachine {
    private String id;
    private String name;
    private String description;
    private boolean alerted;
    private int products;
    private int slots;

    public TechnicianMachine(String id, String name, String description, boolean alerted, int products, int slots) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.alerted = alerted;
        this.products = products;
        this.slots = slots;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAlerted() {
        return alerted;
    }

    public void setAlerted(boolean alerted) {
        this.alerted = alerted;
    }

    public int getProducts() {
        return products;
    }

    public void setProducts(int products) {
        this.products = products;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }
}
