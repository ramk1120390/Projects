package com.Network.Network.DS.DesignPattren.Descorator;

public class CheeseBurstDecorator extends PizzaDecorator {
    public CheeseBurstDecorator(Pizza decoratedPizza) {
        super(decoratedPizza);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " with Cheese Burst";
    }

    @Override
    public double getCost() {
        return super.getCost() + 2.0;
    }
}