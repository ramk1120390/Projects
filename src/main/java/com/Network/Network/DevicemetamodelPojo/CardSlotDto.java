package com.Network.Network.DevicemetamodelPojo;


import java.util.ArrayList;
import java.util.List;

public class CardSlotDto {

    private Long cardslotid;

    private String name;
    private int slotPosition;
    private String operationalState;
    private String administrativeState;
    private String usageState;
    private String href;
    private String realation;
    private String cardname;
    private Card cardid;
    private List<AdditionalAttribute> additionalAttributes = new ArrayList<>();

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

    public Card getCardid() {
        return cardid;
    }

    public void setCardid(Card cardid) {
        this.cardid = cardid;
    }

    public List<AdditionalAttribute> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(List<AdditionalAttribute> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }
}
