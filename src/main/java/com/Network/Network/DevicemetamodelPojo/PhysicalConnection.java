package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PhysicalConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long physicalconnection_id;

    @Column(unique = true, nullable = false)
    private String name;

    private String devicea;

    private String deviceb;
    private String deviceaport;

    private String devicezport;

    private String connectionType;

    private Integer bandwidth;

    private String portnamea;
    private String portnameb;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "physicalconnection_additional_attribute", // Name of the join table
            joinColumns = @JoinColumn(name = "physicalconnection_id"), // Column in the join table referencing Service
            inverseJoinColumns = @JoinColumn(name = "additional_attribute_id") // Column in the join table referencing AdditionalAttribute
    )
    private List<AdditionalAttribute> additionalAttributes = new ArrayList<>();

    public PhysicalConnection() {
    }

    public PhysicalConnection(Long physicalconnection_id, String name, String devicea, String deviceb, String deviceaport, String devicezport, String connectionType, Integer bandwidth, String portnamea, String portnameb, List<AdditionalAttribute> additionalAttributes) {
        this.physicalconnection_id = physicalconnection_id;
        this.name = name;
        this.devicea = devicea;
        this.deviceb = deviceb;
        this.deviceaport = deviceaport;
        this.devicezport = devicezport;
        this.connectionType = connectionType;
        this.bandwidth = bandwidth;
        this.portnamea = portnamea;
        this.portnameb = portnameb;
        this.additionalAttributes = additionalAttributes;
    }

    public Long getPhysicalconnection_id() {
        return physicalconnection_id;
    }

    public void setPhysicalconnection_id(Long physicalconnection_id) {
        this.physicalconnection_id = physicalconnection_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevicea() {
        return devicea;
    }

    public void setDevicea(String devicea) {
        this.devicea = devicea;
    }

    public String getDeviceb() {
        return deviceb;
    }

    public void setDeviceb(String deviceb) {
        this.deviceb = deviceb;
    }

    public String getDeviceaport() {
        return deviceaport;
    }

    public void setDeviceaport(String deviceaport) {
        this.deviceaport = deviceaport;
    }

    public String getDevicezport() {
        return devicezport;
    }

    public void setDevicezport(String devicezport) {
        this.devicezport = devicezport;
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

    public String getPortnamea() {
        return portnamea;
    }

    public void setPortnamea(String portnamea) {
        this.portnamea = portnamea;
    }

    public String getPortnameb() {
        return portnameb;
    }

    public void setPortnameb(String portnameb) {
        this.portnameb = portnameb;
    }

    public List<AdditionalAttribute> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(List<AdditionalAttribute> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }
}
