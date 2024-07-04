package com.Network.Network.DS.DesignPattren.ChainPattern;

public class PayPalPaymentHandler extends PaymentHandler {
    @Override
    public void handlePayment(double amount) {
        if (amount > 1500) {
            System.out.println("Paid using PayPal: $" + amount);
        }
        else {
            System.out.println("PayPal can't handle payments less than or equal to $1500.");
        }
    }
}
