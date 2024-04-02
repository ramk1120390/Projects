package com.Network.Network.excel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DnecPidMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String pid;
    private String model;
    private String description;
    private String profile;
    private String formfactor;
    private String speed;
    private String medium;
    private String reach;
    private String BPIPluggable;
    private String partnumber;
    private String remark;
    private String wavelength;
    private String connection;


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getFormfactor() {
        return formfactor;
    }

    public void setFormfactor(String formfactor) {
        this.formfactor = formfactor;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getReach() {
        return reach;
    }

    public void setReach(String reach) {
        this.reach = reach;
    }

    public String getBPIPluggable() {
        return BPIPluggable;
    }

    public void setBPIPluggable(String BPIPluggable) {
        this.BPIPluggable = BPIPluggable;
    }

    public String getPartnumber() {
        return partnumber;
    }

    public void setPartnumber(String partnumber) {
        this.partnumber = partnumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWavelength() {
        return wavelength;
    }

    public void setWavelength(String wavelength) {
        this.wavelength = wavelength;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public DnecPidMatch() {
    }
}
