package com.Network.Network.DS.DesignPattren.Mediator;

public interface Mediator {
    void sendMessage(String message, Colleague colleague);
}