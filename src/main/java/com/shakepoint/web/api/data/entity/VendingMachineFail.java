package com.shakepoint.web.api.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity(name = "fail")
@Table(name = "fail")
public class VendingMachineFail {

    @Id
    private String id;

    @Column(name = "fail_date")
    private String failDate;

    @Column(name = "message")
    private String message;

    @Column(name = "machine_id")
    private String machineId;

    public VendingMachineFail() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFailDate() {
        return failDate;
    }

    public void setFailDate(String failDate) {
        this.failDate = failDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
}
