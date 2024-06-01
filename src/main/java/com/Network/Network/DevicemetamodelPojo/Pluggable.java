package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pluggable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cardlslotname", referencedColumnName = "name", nullable = true)
    private CardSlot cardSlot;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "devicename", referencedColumnName = "devicename", nullable = true)
    private Device device;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "pluggable_additional_attribute",
            joinColumns = @JoinColumn(name = "pluggable_id"),
            inverseJoinColumns = @JoinColumn(name = "additional_attribute_id")
    )
    private List<AdditionalAttribute> additionalAttributes = new ArrayList<>();
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public CardSlot getCardSlot() {
        return cardSlot;
    }

    public void setCardSlot(CardSlot cardSlot) {
        this.cardSlot = cardSlot;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
