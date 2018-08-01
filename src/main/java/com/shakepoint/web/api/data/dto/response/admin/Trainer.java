package com.shakepoint.web.api.data.dto.response.admin;

public class Trainer {
    private String id;
    private String name;
    private String gym;

    public Trainer(String id, String name, String gym) {
        this.id = id;
        this.name = name;
        this.gym = gym;
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

    public String getGym() {
        return gym;
    }

    public void setGym(String gym) {
        this.gym = gym;
    }
}
