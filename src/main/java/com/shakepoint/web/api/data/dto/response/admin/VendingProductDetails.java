package com.shakepoint.web.api.data.dto.response.admin;

public class VendingProductDetails {
    private String id;
    private String productId;
    private String imageUrl;
    private Integer slot;
    private Integer percentage;
    private String name;

    public VendingProductDetails(String id, String productId, String imageUrl, Integer slot, Integer percentage, String name) {
        this.id = id;
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.slot = slot;
        this.percentage = percentage;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
