package com.Network.Network.DevicemetamodelPojo;

import java.util.ArrayList;
import java.util.List;

public class PluggableDTO {
    private String plugablename;
    private Integer positionOnCard;
    private Integer positionOnDevice;
    private String vendor;
    private String pluggableModel;
    private String pluggablePartNumber;
    private String operationalState;
    private String administrativeState;
    private String usageState;
    private String href;
    private String managementIp;
    private String relation;
    private String cardname;
    private String cardSlotName;
    private Long orderId;
    private String deviceName;
    private List<AdditionalAttribute> additionalAttributes = new ArrayList<>();

    public List<AdditionalAttribute> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(List<AdditionalAttribute> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public String getPlugablename() {
        return plugablename;
    }

    public void setPlugablename(String plugablename) {
        this.plugablename = plugablename;
    }

    public Integer getPositionOnCard() {
        return positionOnCard;
    }

    public void setPositionOnCard(Integer positionOnCard) {
        this.positionOnCard = positionOnCard;
    }

    public Integer getPositionOnDevice() {
        return positionOnDevice;
    }

    public void setPositionOnDevice(Integer positionOnDevice) {
        this.positionOnDevice = positionOnDevice;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getPluggableModel() {
        return pluggableModel;
    }

    public void setPluggableModel(String pluggableModel) {
        this.pluggableModel = pluggableModel;
    }

    public String getPluggablePartNumber() {
        return pluggablePartNumber;
    }

    public void setPluggablePartNumber(String pluggablePartNumber) {
        this.pluggablePartNumber = pluggablePartNumber;
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

    public String getUsageState() {
        return usageState;
    }

    public void setUsageState(String usageState) {
        this.usageState = usageState;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getManagementIp() {
        return managementIp;
    }

    public void setManagementIp(String managementIp) {
        this.managementIp = managementIp;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getCardSlotName() {
        return cardSlotName;
    }

    public void setCardSlotName(String cardSlotName) {
        this.cardSlotName = cardSlotName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}