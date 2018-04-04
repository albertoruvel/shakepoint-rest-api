package com.shakepoint.web.api.data.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "Profile")
@Table(name = "user_profile")
public class UserProfile {
    @Id
    private String id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "weight")
    private double weight;

    @Column(name = "height")
    private double height;

    public UserProfile() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
