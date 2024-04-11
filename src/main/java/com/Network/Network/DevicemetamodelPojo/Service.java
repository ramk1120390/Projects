package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Service implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String operationalState;
    private String administrativeState;
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customername", referencedColumnName = "customername")
    private Customer customer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "service_device",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "device_devicename")
    )
    private List<Device> devices = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "service_additional_attribute", // Name of the join table
            joinColumns = @JoinColumn(name = "service_id"), // Column in the join table referencing Service
            inverseJoinColumns = @JoinColumn(name = "additional_attribute_id") // Column in the join table referencing AdditionalAttribute
    )
    private List<AdditionalAttribute> additionalAttributes = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;


    // Other fields, constructors, getters, and setters
//TODO physical and logical connection


    public Service(Long id, String name, String type, String operationalState, String administrativeState, String notes, Customer customer, List<Device> devices, List<AdditionalAttribute> additionalAttributes, Order order) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.operationalState = operationalState;
        this.administrativeState = administrativeState;
        this.notes = notes;
        this.customer = customer;
        this.devices = devices;
        this.additionalAttributes = additionalAttributes;
        this.order = order;
    }

    public Service() {
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public List<AdditionalAttribute> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(List<AdditionalAttribute> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}