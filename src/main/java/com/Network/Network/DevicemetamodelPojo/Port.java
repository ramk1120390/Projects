package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Entity
public class Port implements Serializable {    //if we are creating port on card need valid with card name and portname
    //pin port card name as cisco
    //pin port card name as  cisco need retict creation
    //if creating
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long portid;
    private String portname;
    private Integer positionOnCard;
    private Integer positionOnDevice;
    private String portType;
    private String operationalState;
    private String administrativeState;
    private String usageState;
    private String href;
    private String PortSpeed;
    private Integer Capacity;
    private String managementIp;
    private String relation;
    private String cardname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cardlslotname", referencedColumnName = "name", nullable = true)
    private CardSlot cardSlot;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devicename", referencedColumnName = "devicename", nullable = true)
    private Device device;

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
        return PortSpeed;
    }

    public void setPortSpeed(String portSpeed) {
        PortSpeed = portSpeed;
    }

    public Integer getCapacity() {
        return Capacity;
    }

    public void setCapacity(Integer capacity) {
        Capacity = capacity;
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
//orelse this shelfid undercard


