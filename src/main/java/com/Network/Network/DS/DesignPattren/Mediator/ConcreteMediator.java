package com.Network.Network.DS.DesignPattren.Mediator;

// ConcreteMediator.java
import java.util.ArrayList;
import java.util.List;

public class ConcreteMediator implements Mediator {
    private List<Colleague> colleagues;

    public ConcreteMediator() {
        this.colleagues = new ArrayList<>();
    }

    public void addColleague(Colleague colleague) {
        colleagues.add(colleague);
    }

    @Override
    public void sendMessage(String message, Colleague sender) {
        for (Colleague colleague : colleagues) {
            // Don't send the message back to the sender
            if (colleague != sender) {
                colleague.receiveMessage(message);
            }
        }
    }
}
