package com.Network.Network.DS.DesignPattren.FacadeDesign;

public class FacadePatternDemo {
    public static void main(String[] args) {
        SwiggyFacade swiggyFacade = new SwiggyFacade();
        swiggyFacade.orderFood("Mumbai", "Restaurant B", "Pizza", "Credit Card");
    }
}