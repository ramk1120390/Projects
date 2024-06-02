package com.Network.Network.DevicemetamodelPojo;

import java.util.ArrayList;

public class PortDTO {
    private Long portid;
    private String portname;
    private Integer positionOnCard;
    private Integer positionOnDevice;
    private String portType;
    private String operationalState;
    private String administrativeState;
    private String usageState;
    private String href;
    private String portSpeed;
    private Integer capacity;
    private String managementIp;
    private String relation;
    private String cardname;
    private String cardslotname; // Assuming you want to include the ID of the card slot
    private Long orderId; // Assuming you want to include the ID of the order
    private String deviceName; // Assuming you want to include the name of the device
    private ArrayList<AdditionalAttribute> additionalAttributes;

    public ArrayList<AdditionalAttribute> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(ArrayList<AdditionalAttribute> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }
    public Long getPortid() {
        return portid;
    }

    public void setPortid(Long portid) {
        this.portid = portid;
    }

    public String getPortname() {
        return portname;
    }

    public void setPortname(String portname) {
        this.portname = portname;
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

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
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

    public String getPortSpeed() {
        return portSpeed;
    }

    public void setPortSpeed(String portSpeed) {
        this.portSpeed = portSpeed;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
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

    public String getCardslotname() {
        return cardslotname;
    }

    public void setCardslotname(String cardslotname) {
        this.cardslotname = cardslotname;
    }
    // Getters and setters
}
