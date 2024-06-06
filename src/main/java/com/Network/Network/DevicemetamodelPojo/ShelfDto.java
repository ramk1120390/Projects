package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class ShelfDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;

    private int shelfPosition;

    private String operationalState;

    private String administrativeState;

    private String href;

    private String usageState;

    private String devicename;

    private List<AdditionalAttribute> additionalAttributes = new ArrayList<>();
    private String realtion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShelfPosition() {
        return shelfPosition;
    }

    public void setShelfPosition(int shelfPosition) {
        this.shelfPosition = shelfPosition;
    }

    public String getOperationalState() {
        return operationalState;
    }

    public void setOperationalState(String operationalState) {
        this.operationalState = operationalState;
    }

    public String getAdministrativeState() {
        return administrativeState;
    }

    public void setAdministrativeState(String administrativeState) {
        this.administrativeState = administrativeState;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getUsageState() {
        return usageState;
    }

    public void setUsageState(String usageState) {
        this.usageState = usageState;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public List<AdditionalAttribute> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(List<AdditionalAttribute> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public String getRealtion() {
        return realtion;
    }

    public void setRealtion(String realtion) {
        this.realtion = realtion;
    }
}
