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


}
