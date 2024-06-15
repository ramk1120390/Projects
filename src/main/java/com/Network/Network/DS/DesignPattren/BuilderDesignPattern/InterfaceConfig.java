package com.Network.Network.DS.DesignPattren.BuilderDesignPattern;

public class InterfaceConfig {
    private String vendor;
    private String loopback;
    private String tunnel;
    private String sw;
    private String remote;
    private String bridgedomain;
    private String vlan;

    public InterfaceConfig(String vendor, String loopback, String tunnel, String sw, String remote, String bridgedomain, String vlan) {
        this.vendor = vendor;
        this.loopback = loopback;
        this.tunnel = tunnel;
        this.sw = sw;
        this.remote = remote;
        this.bridgedomain = bridgedomain;
        this.vlan = vlan;
    }

    public String getVendor() {
        return vendor;
    }

    public String getLoopback() {
        return loopback;
    }

    public String getTunnel() {
        return tunnel;
    }

    public String getSw() {
        return sw;
    }

    public String getRemote() {
        return remote;
    }

    public String getBridgedomain() {
        return bridgedomain;
    }

    public String getVlan() {
        return vlan;
    }
}
