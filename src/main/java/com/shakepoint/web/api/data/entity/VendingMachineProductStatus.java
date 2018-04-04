package com.shakepoint.web.api.data.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "MachineProductStatus")
@Table(name = "machine_product")
public class VendingMachineProductStatus {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "machine_id")
    private VendingMachine machine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @Column(name = "available_percentage")
    private int percentage;

    @Column(name = "slot_number")
    private int  slotNumber;

    public VendingMachineProductStatus() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VendingMachine getMachine() {
        return machine;
    }

    public void setMachine(VendingMachine machine) {
        this.machine = machine;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }
}
