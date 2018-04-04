package com.shakepoint.web.api.data.entity;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity(name = "User")
@Table(name = "user")
public class User {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "is_confirmed")
    private boolean confirmed;

    @Column(name = "creation_date")
    private String creationDate;

    @Column(name = "role")
    private String role;

    @Column(name = "active")
    private boolean active;

    @Column(name = "added_by")
    private String addedBy;

    @Column(name = "last_signin")
    private String lastSignin;

    @Column(name = "access_token")
    private String accessToken;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Purchase> purchases;

    public User() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getLastSignin() {
        return lastSignin;
    }

    public void setLastSignin(String lastSignin) {
        this.lastSignin = lastSignin;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
