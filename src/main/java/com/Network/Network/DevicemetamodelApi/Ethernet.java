package com.Network.Network.DevicemetamodelApi;

public class Ethernet {
    private String name;
    private String desc;
    private String ipaddress;
    private String mediagateway;
    private Boolean status;

    public Ethernet() {
    }

    public Ethernet(String name, String desc, String ipaddress, String mediagateway, Boolean status) {
        this.name = name;
        this.desc = desc;
        this.ipaddress = ipaddress;
        this.mediagateway = mediagateway;
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

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getMediagateway() {
        return mediagateway;
    }

    public void setMediagateway(String mediagateway) {
        this.mediagateway = mediagateway;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Ethernet{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", ipaddress='" + ipaddress + '\'' +
                ", mediagateway='" + mediagateway + '\'' +
                ", status=" + status +
                '}';
    }
}
