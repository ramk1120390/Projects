package com.Network.Network.DevicemetamodelPojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Data
@Getter
@Setter
public class CardDto {
    private Long id;
    private String cardname;
    private String devicename;
    private String slotname;
    private Integer shelfPosition;
    private Integer slotPosition;
    private String vendor;
    private String cardModel;
    private String cardPartNumber;
    private String operationalState;
    private String administrativeState;
    private String usageState;
    private String href;
    private Long orderId;
    @JsonIgnore
    private  String realation;
    private ArrayList<AdditionalAttribute> additionalAttributes;

    public ArrayList<AdditionalAttribute> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(ArrayList<AdditionalAttribute> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public String getSlotname() {
        return slotname;
    }

    public void setSlotname(String slotname) {
        this.slotname = slotname;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getRealation() {
        return realation;
    }

    public void setRealation(String realation) {
        this.realation = realation;
    }
}
