package com.Network.Network.DevicemetamodelApi;

public class GigabitEthernet {
    private String name;
    private String ipaddress;
    private Boolean status;
    private String mediagateway;

    public GigabitEthernet() {
    }

    public GigabitEthernet(String name, String ipaddress, Boolean status, String mediagateway) {
        this.name = name;
        this.ipaddress = ipaddress;
        this.status = status;
        this.mediagateway = mediagateway;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMediagateway() {
        return mediagateway;
    }

    public void setMediagateway(String mediagateway) {
        this.mediagateway = mediagateway;
    }

    @Override
    public String toString() {
        return "GigabitEthernet{" +
                "name='" + name + '\'' +
                ", ipaddress='" + ipaddress + '\'' +
                ", status=" + status +
                ", mediagateway='" + mediagateway + '\'' +
                '}';
    }
}
