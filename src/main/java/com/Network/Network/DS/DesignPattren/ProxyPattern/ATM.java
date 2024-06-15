package com.Network.Network.DS.DesignPattren.ProxyPattern;

public class ATM implements Account {
    private BankAccount bankAccount;

    public ATM(String accountNumber, double initialBalance) {
        this.bankAccount = new BankAccount(accountNumber, initialBalance);
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("Processing withdrawal through ATM...");
        bankAccount.withdraw(amount);
    }

    @Override
    public String getAccountNumber() {
        return bankAccount.getAccountNumber();
    }
}