package com.Network.Network.DevicemetamodelPojo;

import java.util.ArrayList;

public class CienaPort {
    private String name;
    private String description;
    private String mtu;
    private Boolean status;
    private ArrayList<String> vlanid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMtu() {
        return mtu;
    }

    public void setMtu(String mtu) {
        this.mtu = mtu;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ArrayList<String> getVlanid() {
        return vlanid;
    }

    public void setVlanid(ArrayList<String> vlanid) {
        this.vlanid = vlanid;
    }

    public CienaPort(String name, String description, String mtu, Boolean status, ArrayList<String> vlanid) {
        this.name = name;
        this.description = description;
        this.mtu = mtu;
        this.status = status;
        this.vlanid = vlanid;
    }

    public CienaPort() {
    }
}
