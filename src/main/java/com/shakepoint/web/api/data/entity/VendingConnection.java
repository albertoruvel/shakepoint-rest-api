package com.shakepoint.web.api.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity(name = "VendingConnection")
@Table(name = "machine_connection")
public class VendingConnection {

    @Id
    private String id;

    @Column(name = "port")
    private int port;

    @Column(name = "connection_active")
    private Boolean active;

    @Column(name ="machine_id")
    private String machineId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
}
