package com.Network.Network.DS.DesignPattren.BuilderDesignPattern;

public class InterFaceDirector {
    private InterFaceBuilder interFaceBuilder;

    public InterFaceDirector(InterFaceBuilder interFaceBuilder) {
        this.interFaceBuilder = interFaceBuilder;
    }

    public InterFaces makeInterFace() {
        interFaceBuilder.addLoopback();
        interFaceBuilder.addTunnel();
        interFaceBuilder.addSw();
        interFaceBuilder.addRemote();
        interFaceBuilder.addBridgeDomain();
        interFaceBuilder.addVlan();
        return interFaceBuilder.build();
    }
}
