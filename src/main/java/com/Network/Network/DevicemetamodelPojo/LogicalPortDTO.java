package com.Network.Network.DevicemetamodelPojo;

import java.util.List;

public class LogicalPortDTO {
    private Long logicalPortId;
    private String name;
    private Integer positionOnCard;
    private Integer positionOnDevice;
    private String portType;
    private String operationalState;
    private String administrativeState;
    private String usageState;
    private String href;
    private String portSpeed;
    private Integer capacity;
    private Integer positionOnPort;
    private String managementIp;
    private String deviceName; // ID of the associated Device
    private Long orderId; // ID of the associated Order
    private Long pluggableId; // ID of the associated Pluggable
    private Long portId; // ID of the associated Port

    private List<AdditionalAttribute> additionalAttribute;

    public Long getLogicalPortId() {
        return logicalPortId;
    }

    public void setLogicalPortId(Long logicalPortId) {
        this.logicalPortId = logicalPortId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getPositionOnPort() {
        return positionOnPort;
    }

    public void setPositionOnPort(Integer positionOnPort) {
        this.positionOnPort = positionOnPort;
    }

    public String getManagementIp() {
        return managementIp;
    }

    public void setManagementIp(String managementIp) {
        this.managementIp = managementIp;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getPluggableId() {
        return pluggableId;
    }

    public void setPluggableId(Long pluggableId) {
        this.pluggableId = pluggableId;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public List<AdditionalAttribute> getAdditionalAttribute() {
        return additionalAttribute;
    }

    public void setAdditionalAttribute(List<AdditionalAttribute> additionalAttribute) {
        this.additionalAttribute = additionalAttribute;
    }
}
