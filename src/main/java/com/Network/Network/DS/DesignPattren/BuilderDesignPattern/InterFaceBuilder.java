package com.Network.Network.DS.DesignPattren.BuilderDesignPattern;

public abstract class InterFaceBuilder {
    public abstract void addLoopback();

    public abstract void addTunnel();

    public abstract void addSw();

    public abstract void addRemote();

    public abstract void addBridgeDomain();

    public abstract void addVlan();

    public abstract InterFaces build();
}
