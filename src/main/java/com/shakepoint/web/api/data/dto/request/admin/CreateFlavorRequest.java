package com.shakepoint.web.api.data.dto.request.admin;

public class CreateFlavorRequest {
    private String name;
    private String hexColor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }
}
