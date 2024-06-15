package com.Network.Network.DS.DesignPattren.Mediator;

public class ConcreteColleague1 extends Colleague{
    public ConcreteColleague1(Mediator mediator) {
        super(mediator);
    }

    @Override
    public void receiveMessage(String message) {
        System.out.println("ConcreteColleague1 received: " + message);
    }
}
