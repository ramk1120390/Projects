package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
public class Port {    //if we are creating port on card need valid with card name and portname
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
//orelse this shelfid undercard


