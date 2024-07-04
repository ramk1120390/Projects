package com.Network.Network.DS.DesignPattren.Mediator;

// MediatorPatternDemo.java
public class MediatorPatternDemo {
    public static void main(String[] args) {
        ConcreteMediator mediator = new ConcreteMediator();

        ConcreteColleague1 colleague1 = new ConcreteColleague1(mediator);
        ConcreteColleague2 colleague2 = new ConcreteColleague2(mediator);

        mediator.addColleague(colleague1);
        mediator.addColleague(colleague2);

        colleague1.sendMessage("Hello from Colleague 1");
        colleague2.sendMessage("Hi, this is Colleague 2");
    }
}
