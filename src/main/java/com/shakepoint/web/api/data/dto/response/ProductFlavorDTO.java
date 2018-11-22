package com.shakepoint.web.api.data.dto.response;

public class ProductFlavorDTO {
    private String id;
    private String color;
    private String name;

    public ProductFlavorDTO(String id, String color, String name) {
        this.id = id;
        this.color = color;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
