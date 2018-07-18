package com.shakepoint.web.api.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Table(name = "user_password")
@Entity(name = "UserPassword")
public class UserPassword {

    @Id
    private String id;

    @Column(name = "password_token")
    private String token;

    @Column(name = "token_expiration")
    private String expirationDate;

    @Column(name = "reset_count")
    private Integer resetCount;

    @Column(name = "user_id")
    private String userId;

    public UserPassword() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getResetCount() {
        return resetCount;
    }

    public void setResetCount(Integer resetCount) {
        this.resetCount = resetCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
