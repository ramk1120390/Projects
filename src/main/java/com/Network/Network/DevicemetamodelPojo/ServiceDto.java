package com.Network.Network.DevicemetamodelPojo;



import java.util.ArrayList;

public class ServiceDto {
    private Long id;
    private String name;
    private String type;
    private String operationalState;
    private String administrativeState;
    private String notes;
    // private ArrayList<String> logicalConnections;


    //private ArrayList<String> physicalConnections;

    private String customer;

    private ArrayList<String> devices;

    private ArrayList<String> logicalconnection;
    private ArrayList<String> physicalconnection;

    private ArrayList<AdditionalAttribute> additionalAttributes;



    public ServiceDto() {
    }

    public ServiceDto(Long id, String name, String type, String operationalState, String administrativeState, String notes, String customer, ArrayList<String> devices, ArrayList<String> logicalconnection, ArrayList<String> physicalconnection, ArrayList<AdditionalAttribute> additionalAttributes) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.operationalState = operationalState;
        this.administrativeState = administrativeState;
        this.notes = notes;
        this.customer = customer;
        this.devices = devices;
        this.logicalconnection = logicalconnection;
        this.physicalconnection = physicalconnection;
        this.additionalAttributes = additionalAttributes;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public ArrayList<String> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<String> devices) {
        this.devices = devices;
    }

    public ArrayList<AdditionalAttribute> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(ArrayList<AdditionalAttribute> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public ArrayList<String> getLogicalconnection() {
        return logicalconnection;
    }

    public void setLogicalconnection(ArrayList<String> logicalconnection) {
        this.logicalconnection = logicalconnection;
    }

    public ArrayList<String> getPhysicalconnection() {
        return physicalconnection;
    }

    public void setPhysicalconnection(ArrayList<String> physicalconnection) {
        this.physicalconnection = physicalconnection;
    }
}
