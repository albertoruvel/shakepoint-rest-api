package com.shakepoint.web.api.data.entity;

import com.shakepoint.web.api.core.machine.PurchaseStatus;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "Purchase")
@Table(name = "purchase")
public class Purchase {
    @Id
    private String id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private PurchaseStatus status;

    @Column(name = "purchase_date")
    private String purchaseDate;

    @Column(name = "total")
    private double total;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "machine_id")
    private VendingMachine machine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "reference")
    private String reference;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "qr_image_url")
    private String qrCodeUrl;

    @Column(name = "control_number")
    private String controlNumber;

    public Purchase() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PurchaseStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseStatus status) {
        this.status = status;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public VendingMachine getMachine() {
        return machine;
    }

    public void setMachine(VendingMachine machine) {
        this.machine = machine;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getControlNumber() {
        return controlNumber;
    }

    public void setControlNumber(String controlNumber) {
        this.controlNumber = controlNumber;
    }
}
