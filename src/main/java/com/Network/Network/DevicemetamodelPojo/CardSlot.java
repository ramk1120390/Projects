package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;

@Entity
public class CardSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardslotid;
    @Column(nullable = false, unique = true)
    private String name;
    private int slotPosition;
    private String operationalState;
    private String administrativeState;
    private String usageState;
    private String href;
    private String realation;

    private String cardname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cardid", referencedColumnName = "cardid")
    private Card card;

    public Long getCardslotid() {
        return cardslotid;
    }

    public void setCardslotid(Long cardslotid) {
        this.cardslotid = cardslotid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSlotPosition() {
        return slotPosition;
    }

    public void setSlotPosition(int slotPosition) {
        this.slotPosition = slotPosition;
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

    public String getRealation() {
        return realation;
    }

    public void setRealation(String realation) {
        this.realation = realation;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
