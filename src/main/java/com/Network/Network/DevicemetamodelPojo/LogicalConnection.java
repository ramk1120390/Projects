package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class LogicalConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logicalconnection_id;

    private String name;

    private String deviceA;

    private String deviceZ;

    private ArrayList<String> devicesConnected;


    private String connectionType;

    private Integer bandwidth;

    private String deviceALogicalPort;

    private String deviceZLogicalPort;

    private ArrayList<String> physicalConnections;

    private String deviceALogicalPortName;
    private String deviceZLogicalPortName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "logicalconnection_additional_attribute", // Name of the join table
            joinColumns = @JoinColumn(name = "logicalconnection_id"), // Column in the join table referencing Service
            inverseJoinColumns = @JoinColumn(name = "additional_attribute_id") // Column in the join table referencing AdditionalAttribute
    )
    private List<AdditionalAttribute> additionalAttributes = new ArrayList<>();

    public Long getLogicalconnection_id() {
        return logicalconnection_id;
    }

    public void setLogicalconnection_id(Long logicalconnection_id) {
        this.logicalconnection_id = logicalconnection_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceA() {
        return deviceA;
    }

    public void setDeviceA(String deviceA) {
        this.deviceA = deviceA;
    }

    public String getDeviceZ() {
        return deviceZ;
    }

    public void setDeviceZ(String deviceZ) {
        this.deviceZ = deviceZ;
    }

    public ArrayList<String> getDevicesConnected() {
        return devicesConnected;
    }

    public void setDevicesConnected(ArrayList<String> devicesConnected) {
        this.devicesConnected = devicesConnected;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public Integer getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Integer bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getDeviceALogicalPort() {
        return deviceALogicalPort;
    }

    public void setDeviceALogicalPort(String deviceALogicalPort) {
        this.deviceALogicalPort = deviceALogicalPort;
    }

    public String getDeviceZLogicalPort() {
        return deviceZLogicalPort;
    }

    public void setDeviceZLogicalPort(String deviceZLogicalPort) {
        this.deviceZLogicalPort = deviceZLogicalPort;
    }

    public ArrayList<String> getPhysicalConnections() {
        return physicalConnections;
    }

    public void setPhysicalConnections(ArrayList<String> physicalConnections) {
        this.physicalConnections = physicalConnections;
    }

    public List<AdditionalAttribute> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(List<AdditionalAttribute> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public LogicalConnection(Long logicalconnection_id, String name, String deviceA, String deviceZ, ArrayList<String> devicesConnected, String connectionType, Integer bandwidth, String deviceALogicalPort, String deviceZLogicalPort, ArrayList<String> physicalConnections, String deviceALogicalPortName, String deviceZLogicalPortName, List<AdditionalAttribute> additionalAttributes) {
        this.logicalconnection_id = logicalconnection_id;
        this.name = name;
        this.deviceA = deviceA;
        this.deviceZ = deviceZ;
        this.devicesConnected = devicesConnected;
        this.connectionType = connectionType;
        this.bandwidth = bandwidth;
        this.deviceALogicalPort = deviceALogicalPort;
        this.deviceZLogicalPort = deviceZLogicalPort;
        this.physicalConnections = physicalConnections;
        this.deviceALogicalPortName = deviceALogicalPortName;
        this.deviceZLogicalPortName = deviceZLogicalPortName;
        this.additionalAttributes = additionalAttributes;
    }

    public LogicalConnection() {
    }
}
