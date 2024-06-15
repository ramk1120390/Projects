package com.Network.Network.DS.DesignPattren.TemplatePattern;

public class InStoreOrderProcess extends OrderProcess {

    @Override
    void selectProduct() {
        System.out.println("Product selected in physical store.");
    }

    @Override
    void doPayment() {
        System.out.println("Payment done at the counter.");
    }

    @Override
    boolean isGift() {
        return true;
    }

    @Override
    void wrapGift() {
        System.out.println("Gift wrapped at the store.");
    }
}
