package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class LogicalPort implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logicalportid;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devicename", referencedColumnName = "devicename", nullable = true)
    private Device device;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plugableid", referencedColumnName = "id", nullable = true)
    private Pluggable pluggable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portid", referencedColumnName = "portid", nullable = true)
    private Port port;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "logicalPort_additional_attribute", // Name of the join table
            joinColumns = @JoinColumn(name = "logicalPort_id"), // Column in the join table referencing Service
            inverseJoinColumns = @JoinColumn(name = "additional_attribute_id") // Column in the join table referencing AdditionalAttribute
    )
    private List<AdditionalAttribute> additionalAttributes = new ArrayList<>();

    public LogicalPort(Long logicalportid, String name, Integer positionOnCard, Integer positionOnDevice, String portType, String operationalState, String administrativeState, String usageState, String href, String portSpeed, Integer capacity, Integer positionOnPort, String managementIp, Device device, Order order, Pluggable pluggable, Port port, List<AdditionalAttribute> additionalAttributes) {
        this.logicalportid = logicalportid;
        this.name = name;
        this.positionOnCard = positionOnCard;
        this.positionOnDevice = positionOnDevice;
        this.portType = portType;
        this.operationalState = operationalState;
        this.administrativeState = administrativeState;
        this.usageState = usageState;
        this.href = href;
        this.portSpeed = portSpeed;
        this.capacity = capacity;
        this.positionOnPort = positionOnPort;
        this.managementIp = managementIp;
        this.device = device;
        this.order = order;
        this.pluggable = pluggable;
        this.port = port;
        this.additionalAttributes = additionalAttributes;
    }

    public LogicalPort() {
    }

    public Long getLogicalportid() {
        return logicalportid;
    }

    public void setLogicalportid(Long logicalportid) {
        this.logicalportid = logicalportid;
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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Pluggable getPluggable() {
        return pluggable;
    }

    public void setPluggable(Pluggable pluggable) {
        this.pluggable = pluggable;
    }

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public List<AdditionalAttribute> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(List<AdditionalAttribute> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }
}
