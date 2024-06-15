package com.Network.Network.DS.DesignPattren.ChainPattern;

public class CreditCardPaymentHandler extends PaymentHandler {
    @Override
    public void handlePayment(double amount) {
        if (amount <= 1500) {
            System.out.println("Paid using credit card: $" + amount);
        } else {
            next.handlePayment(amount);
        }
    }
}
