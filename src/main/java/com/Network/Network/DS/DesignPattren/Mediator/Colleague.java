package com.Network.Network.DS.DesignPattren.Mediator;

// Colleague.java
public abstract class Colleague {
    protected Mediator mediator;

    public Colleague(Mediator mediator) {
        this.mediator = mediator;
    }

    public void sendMessage(String message) {
        mediator.sendMessage(message, this);
    }

    public abstract void receiveMessage(String message);
}
