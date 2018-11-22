package com.shakepoint.web.api.data.entity;

import com.shakepoint.web.api.core.machine.ProductType;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "Product")
@Table(name = "product")
public class Product {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "price")
    private double price;

    @Column(name = "creation_date")
    private String creationDate;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private ProductType type;

    @Column(name = "engine_use_time")
    private String engineUseTime;

    @Column(name = "nutritional_data_url")
    private String nutritionalDataUrl;

    @Column(name = "mix_time")
    private Integer mixTime;

    public Product() {
        id = UUID.randomUUID().toString();
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductType getType() {
        return type;
    }


    public String getEngineUseTime() {
        return engineUseTime;
    }

    public void setEngineUseTime(String engineUseTime) {
        this.engineUseTime = engineUseTime;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getNutritionalDataUrl() {
        return nutritionalDataUrl;
    }

    public void setNutritionalDataUrl(String nutritionalDataUrl) {
        this.nutritionalDataUrl = nutritionalDataUrl;
    }

    public Integer getMixTime() {
        return mixTime;
    }

    public void setMixTime(Integer mixTime) {
        this.mixTime = mixTime;
    }
}
