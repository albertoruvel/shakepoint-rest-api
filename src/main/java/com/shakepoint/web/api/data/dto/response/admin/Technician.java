package com.shakepoint.web.api.data.dto.response.admin;
public class Technician {
    private String id;
    private String name;
    private String email;
    private String creationDate;
    private boolean active;

    public Technician(String id, String name, String email, String creationDate, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.creationDate = creationDate;
        this.active = active;
    }

    public Technician() {
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


}
