package com.Network.Network.DS.DesignPattren.BuilderDesignPattern;

public class CiscoInterFaceBuilder extends InterFaceBuilder {
    private InterFaces interFaces;
    private InterfaceConfig config;

    public CiscoInterFaceBuilder(InterfaceConfig config) {
        interFaces = new InterFaces();
        this.config = config;
    }

    @Override
    public void addLoopback() {
        this.interFaces.setLoopback(config.getLoopback());
    }

    @Override
    public void addTunnel() {
        this.interFaces.setTunnel(config.getTunnel());
    }

    @Override
    public void addSw() {
        this.interFaces.setSw(null);
    }

    @Override
    public void addRemote() {
        this.interFaces.setRemote(config.getRemote());
    }

    @Override
    public void addBridgeDomain() {
        this.interFaces.setBridgedomain(config.getBridgedomain());
    }

    @Override
    public void addVlan() {
        this.interFaces.setVlan(config.getVlan());
    }

    @Override
    public InterFaces build() {
        return interFaces;
    }
}
