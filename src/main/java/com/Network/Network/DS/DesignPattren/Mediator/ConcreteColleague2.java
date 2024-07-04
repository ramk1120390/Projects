package com.Network.Network.DS.DesignPattren.Mediator;

// ConcreteColleague2.java
public class ConcreteColleague2 extends Colleague {
    public ConcreteColleague2(Mediator mediator) {
        super(mediator);
    }

    @Override
    public void receiveMessage(String message) {
        System.out.println("ConcreteColleague2 received: " + message);
    }
}
