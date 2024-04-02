package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Embeddable
public class CardId implements Serializable {

    private String cardname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devicename", referencedColumnName = "devicename")
    private Device device;

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}