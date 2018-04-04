package com.shakepoint.web.api.data.entity;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity(name = "Machine")
@Table(name = "machine")
public class VendingMachine {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @Column(name = "creation_date")
    private String creationDate;

    @Column(name = "added_by")
    private String addedBy;

    @Column(name = "active")
    private boolean active;

    @Column(name = "slots")
    private int slots;

    @Column(name = "has_error")
    private boolean error;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "technician_id")
    private User technician;

    @OneToMany(mappedBy = "machine")
    private List<VendingMachineProductStatus> products;



    public VendingMachine() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public User getTechnician() {
        return technician;
    }

    public void setTechnician(User technician) {
        this.technician = technician;
    }

    public List<VendingMachineProductStatus> getProducts() {
        return products;
    }

    public void setProducts(List<VendingMachineProductStatus> products) {
        this.products = products;
    }
}
