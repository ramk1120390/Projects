package com.Network.Network.DS.DesignPattren.Descorator;

public class JalepanoDecorator extends PizzaDecorator {
    public JalepanoDecorator(Pizza decoratedPizza) {
        super(decoratedPizza);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " with Jalapeno";
    }

    @Override
    public double getCost() {
        return super.getCost() + 1.5;
    }
}
