package com.shakepoint.web.api.data.dto.response.admin;

public class SimpleMachine {
    private String id;
    private String name;
    private String description;
    private String partnerName;
    private int workingPort;
    private boolean active;
    public SimpleMachine() {
    }

    public SimpleMachine(String id, String name, String description, String partnerName, int workingPort, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.partnerName = partnerName;
        this.workingPort = workingPort;
        this.active = active;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public int getWorkingPort() {
        return workingPort;
    }

    public void setWorkingPort(int workingPort) {
        this.workingPort = workingPort;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
