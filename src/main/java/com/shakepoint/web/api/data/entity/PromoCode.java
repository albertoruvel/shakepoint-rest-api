package com.shakepoint.web.api.data.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "Promo")
@Table(name = "promocode")
public class PromoCode {

    @Id
    private String id;

    @Column(name = "description")
    private String description;

    @Column(name = "promo_code")
    private String code;

    @Column(name = "promo_type")
    private Integer type;

    @Column(name = "discount")
    private int discount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "expiration_date")
    private String expirationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trainer_id")
    private User trainer;

    @Column(name = "active")
    private Integer status;

    public PromoCode() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getTrainer() {
        return trainer;
    }

    public void setTrainer(User trainer) {
        this.trainer = trainer;
    }

    public PromoCodeStatus getStatus() {
        return PromoCodeStatus.fromValue(this.status);
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
