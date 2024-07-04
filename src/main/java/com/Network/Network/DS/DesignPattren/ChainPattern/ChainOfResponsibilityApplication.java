package com.Network.Network.DS.DesignPattren.ChainPattern;

public class ChainOfResponsibilityApplication {
    public static void main(String[] args) {
        PaymentHandler bank = new BankPaymentHandler();
        PaymentHandler creditCard = new CreditCardPaymentHandler();
        PaymentHandler paypal = new PayPalPaymentHandler();
        bank.setNext(creditCard);
        creditCard.setNext(paypal);

        bank.handlePayment(600);
        bank.handlePayment(200);
        bank.handlePayment(1200);
        bank.handlePayment(600);
        bank.handlePayment(10000);
    }

}
