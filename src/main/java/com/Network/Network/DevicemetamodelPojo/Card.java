package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Card implements Serializable {
    @Column(unique = true, nullable = false)
    private Long cardid;

    @EmbeddedId
    private CardId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slotname", referencedColumnName = "slotname")
    private Slot slot;

    private Integer shelfPosition;
    private Integer slotPosition;
    private String vendor;
    private String cardModel;
    private String cardPartNumber;
    private String operationalState;
    private String administrativeState;
    private String usageState;
    private String href;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    private String realation;

    public Long getCardid() {
        return cardid;
    }

    public void setCardid(Long cardid) {
        this.cardid = cardid;
    }

    public CardId getId() {
        return id;
    }

    public void setId(CardId id) {
        this.id = id;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public Integer getShelfPosition() {
        return shelfPosition;
    }

    public void setShelfPosition(Integer shelfPosition) {
        this.shelfPosition = shelfPosition;
    }

    public Integer getSlotPosition() {
        return slotPosition;
    }

    public void setSlotPosition(Integer slotPosition) {
        this.slotPosition = slotPosition;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getCardModel() {
        return cardModel;
    }

    public void setCardModel(String cardModel) {
        this.cardModel = cardModel;
    }

    public String getCardPartNumber() {
        return cardPartNumber;
    }

    public void setCardPartNumber(String cardPartNumber) {
        this.cardPartNumber = cardPartNumber;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getRealation() {
        return realation;
    }

    public void setRealation(String realation) {
        this.realation = realation;
    }
}