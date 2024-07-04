package com.Network.Network.DS.DesignPattren.ProxyPattern;

public class ProxyPatternDemo {
    public static void main(String[] args) {
        Account atm = new ATM("1234567890", 1000.0);

        // Display account number
        System.out.println("Account Number: " + atm.getAccountNumber());

        // Attempt to withdraw money
        atm.withdraw(100.0);
        atm.withdraw(950.0); // This should trigger insufficient funds
    }
}