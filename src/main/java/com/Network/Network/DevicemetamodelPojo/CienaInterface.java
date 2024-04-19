package com.Network.Network.DevicemetamodelPojo;

import java.util.ArrayList;

public class CienaInterface {
    private String name;
    private String vlanid;
    private ArrayList<String> ipaddrees;

    public CienaInterface(String name, String vlanid, ArrayList<String> ipaddrees) {
        this.name = name;
        this.vlanid = vlanid;
        this.ipaddrees = ipaddrees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVlanid() {
        return vlanid;
    }

    public void setVlanid(String vlanid) {
        this.vlanid = vlanid;
    }

    public ArrayList<String> getIpaddrees() {
        return ipaddrees;
    }

    public void setIpaddrees(ArrayList<String> ipaddrees) {
        this.ipaddrees = ipaddrees;
    }

    public CienaInterface() {
    }
}
