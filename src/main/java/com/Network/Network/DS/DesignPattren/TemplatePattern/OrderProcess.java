package com.Network.Network.DS.DesignPattren.TemplatePattern;

public abstract class OrderProcess {
    // Template method
    public final void processOrder() {
        selectProduct();
        doPayment();
        if (isGift()) {
            wrapGift();
        }
        deliver();
    }

    // Abstract methods (hooks)
    abstract void selectProduct();

    abstract void doPayment();

    // Hooked method
    void wrapGift() {
        System.out.println("Gift wrapped.");
    }

    // Concrete method
    void deliver() {
        System.out.println("Product delivered to the customer.");
    }

    // Hook method
    boolean isGift() {
        return false;
    }
}
