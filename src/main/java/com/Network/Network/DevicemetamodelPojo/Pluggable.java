package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;

@Entity
public class Pluggable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devicename", referencedColumnName = "devicename", nullable = true)
    private Device device;

    public void setDevice(Device device) {
        this.device = device;
        if (device != null) {
            this.cardSlot = null; // Set cardSlot to null when device is set
        }
    }

    public void setCardSlot(CardSlot cardSlot) {
        this.cardSlot = cardSlot;
        if (cardSlot != null) {
            this.device = null; // Set device to null when cardSlot is set
        }
    }
}
