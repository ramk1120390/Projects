package com.Network.Network.DS.DesignPattren.TemplatePattern;
public class TemplatePatternExample {

    public static void main(String[] args) {
        System.out.println("Online Order Process:");
        OrderProcess onlineOrder = new OnlineOrderProcess();
        onlineOrder.processOrder();

        System.out.println("\nIn-store Order Process:");
        OrderProcess inStoreOrder = new InStoreOrderProcess();
        inStoreOrder.processOrder();
    }
}