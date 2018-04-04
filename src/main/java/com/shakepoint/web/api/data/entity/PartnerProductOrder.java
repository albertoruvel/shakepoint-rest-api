package com.shakepoint.web.api.data.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "Order")
@Table(name = "partner_order")
public class PartnerProductOrder {

    @Id
    private String id;

    @Column(name = "order_date")
    private String orderDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "partner_id")
    private User partner;

    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "status")
    private PartnerProductOrderStatus status;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<PartnerProductItem> products;

    @Column(name = "machine_id")
    private String machineId;

    public PartnerProductOrder() {
        this.id = UUID.randomUUID().toString();
        products = new ArrayList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public User getPartner() {
        return partner;
    }

    public void setPartner(User partner) {
        this.partner = partner;
    }

    public PartnerProductOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PartnerProductOrderStatus status) {
        this.status = status;
    }

    public List<PartnerProductItem> getProducts() {
        return products;
    }

    public void setProducts(List<PartnerProductItem> products) {
        this.products = products;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
}
