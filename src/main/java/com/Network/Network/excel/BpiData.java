package com.Network.Network.excel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BpiData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String bpiCard;
    private String netboxCard;
    private String bpiPartnumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBpiCard() {
        return bpiCard;
    }

    public void setBpiCard(String bpiCard) {
        this.bpiCard = bpiCard;
    }

    public String getNetboxCard() {
        return netboxCard;
    }

    public void setNetboxCard(String netboxCard) {
        this.netboxCard = netboxCard;
    }

    public String getBpiPartnumber() {
        return bpiPartnumber;
    }

    public void setBpiPartnumber(String bpiPartnumber) {
        this.bpiPartnumber = bpiPartnumber;
    }
}
