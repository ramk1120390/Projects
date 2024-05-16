package com.Network.Network.DevicemetamodelApi;

public class Pri {
    private String name;
    private String desc;
    private Boolean status;

    public Pri() {
    }

    public Pri(String name, String desc, Boolean status) {
        this.name = name;
        this.desc = desc;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Pri{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", status=" + status +
                '}';
    }
}
