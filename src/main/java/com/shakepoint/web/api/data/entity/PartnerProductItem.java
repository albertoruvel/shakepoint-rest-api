package com.shakepoint.web.api.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity(name = "OrderProduct")
@Table(name = "order_product")
public class PartnerProductItem {

    @Id
    private String id;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "quantity")
    private Integer quantity;


    public PartnerProductItem() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
