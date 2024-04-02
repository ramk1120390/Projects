package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String slotname;
    private int slotPosition;
    private String operationalState;
    private String administrativeState;
    private String usageState;
    private String href;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelfname", referencedColumnName = "name")
    private Shelf shelf;
    private String relation;
    //TODO Slotname and shelfname nee change to be composite //or change into device name into composite


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlotname() {
        return slotname;
    }

    public void setSlotname(String slotname) {
        this.slotname = slotname;
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

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
