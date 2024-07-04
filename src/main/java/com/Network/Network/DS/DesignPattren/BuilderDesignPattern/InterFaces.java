package com.Network.Network.DS.DesignPattren.BuilderDesignPattern;

public class InterFaces {
    private String loopback;
    private String tunnel;
    private String sw;
    private String remote;
    private String bridgedomain;
    private String vlan;

    public String getLoopback() {
        return loopback;
    }

    public void setLoopback(String loopback) {
        this.loopback = loopback;
    }

    public String getTunnel() {
        return tunnel;
    }

    public void setTunnel(String tunnel) {
        this.tunnel = tunnel;
    }

    public String getSw() {
        return sw;
    }

    public void setSw(String sw) {
        this.sw = sw;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public String getBridgedomain() {
        return bridgedomain;
    }

    public void setBridgedomain(String bridgedomain) {
        this.bridgedomain = bridgedomain;
    }

    public String getVlan() {
        return vlan;
    }

    public void setVlan(String vlan) {
        this.vlan = vlan;
    }

    @Override
    public String toString() {
        return "InterFaces{" +
                "loopback='" + loopback + '\'' +
                ", tunnel='" + tunnel + '\'' +
                ", sw='" + sw + '\'' +
                ", remote='" + remote + '\'' +
                ", bridgedomain='" + bridgedomain + '\'' +
                ", vlan='" + vlan + '\'' +
                '}';
    }
}
