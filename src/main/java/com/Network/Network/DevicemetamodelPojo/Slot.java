package com.Network.Network.DevicemetamodelPojo;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Slot implements Serializable {
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
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "slot_additional_attribute",
            joinColumns = @JoinColumn(name = "slot_id"),
            inverseJoinColumns = @JoinColumn(name = "additional_attribute_id")
    )
    private List<AdditionalAttribute> additionalAttributes = new ArrayList<>();

    public Slot(Long id, String slotname, int slotPosition, String operationalState, String administrativeState, String usageState, String href, Shelf shelf, String relation, List<AdditionalAttribute> additionalAttributes) {
        this.id = id;
        this.slotname = slotname;
        this.slotPosition = slotPosition;
        this.operationalState = operationalState;
        this.administrativeState = administrativeState;
        this.usageState = usageState;
        this.href = href;
        this.shelf = shelf;
        this.relation = relation;
        this.additionalAttributes = additionalAttributes;
    }

    public Slot() {
        super();
    }

    public List<AdditionalAttribute> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(List<AdditionalAttribute> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

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
