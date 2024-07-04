package com.Network.Network.DS.DesignPattren.Descorator;

public class BasePizza implements Pizza {
    @Override
    public String getDescription() {
        return "Pizza";
    }

    @Override
    public double getCost() {
        return 5.0;
    }
}
