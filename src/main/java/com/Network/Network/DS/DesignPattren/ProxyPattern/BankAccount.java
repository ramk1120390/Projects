package com.Network.Network.DS.DesignPattren.ProxyPattern;

public class BankAccount implements Account {
    private String accountNumber;
    private double balance;

    public BankAccount(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    @Override
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Withdrew " + amount + ". New balance: " + balance);
        } else {
            System.out.println("Insufficient funds. Current balance: " + balance);
        }
    }

    @Override
    public String getAccountNumber() {
        return accountNumber;
    }
}