package com.Network.Network.DevicemetamodelPojo;



import java.util.ArrayList;
import java.util.List;

public class SlotDto{

    private Long id;

    private String slotname;
    private int slotPosition;
    private String operationalState;
    private String administrativeState;
    private String usageState;
    private String href;

    private String shelfname;
    private String relation;

    private List<AdditionalAttribute> additionalAttributes = new ArrayList<>();




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

    public String getShelfname() {
        return shelfname;
    }

    public void setShelfname(String shelfname) {
        this.shelfname = shelfname;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
