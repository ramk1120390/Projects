package com.Network.Network.DS.DesignPattren.BuilderDesignPattern;

public class CienaInterfaceBuilder extends InterFaceBuilder {
    private InterFaces interFaces;
    private InterfaceConfig config;

    public CienaInterfaceBuilder(InterfaceConfig config) {
        interFaces = new InterFaces();
        this.config = config;
    }

    @Override
    public void addLoopback() {
        this.interFaces.setLoopback(null);
    }

    @Override
    public void addTunnel() {
        this.interFaces.setTunnel(null);
    }

    @Override
    public void addSw() {
        this.interFaces.setSw(config.getSw());
    }

    @Override
    public void addRemote() {
        this.interFaces.setRemote(config.getRemote());
    }

    @Override
    public void addBridgeDomain() {
        this.interFaces.setBridgedomain(null);
    }

    @Override
    public void addVlan() {
        this.interFaces.setVlan(config.getSw());  // Use SW data for VLAN
    }

    @Override
    public InterFaces build() {
        return interFaces;
    }

}
