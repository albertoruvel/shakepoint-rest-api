package com.shakepoint.web.api.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity(name = "Trainer")
@Table(name = "user_trainer")
public class TrainerInformation {

    @Id
    private String id;

    @Column(name = "partner_id")
    private String partner;

    @Column(name = "user_id")
    private String trainerUser;

    @Column(name = "registration_date")
    private String registrationDate;

    @Column(name = "active")
    private boolean active;

    public TrainerInformation() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getTrainerUser() {
        return trainerUser;
    }

    public void setTrainerUser(String trainerUser) {
        this.trainerUser = trainerUser;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
