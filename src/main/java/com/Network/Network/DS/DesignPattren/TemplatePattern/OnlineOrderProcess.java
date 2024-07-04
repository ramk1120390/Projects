package com.Network.Network.DS.DesignPattren.TemplatePattern;

public class OnlineOrderProcess extends OrderProcess {

    @Override
    void selectProduct() {
        System.out.println("Product selected in online store.");
    }

    @Override
    void doPayment() {
        System.out.println("Payment done using credit card.");
    }
}