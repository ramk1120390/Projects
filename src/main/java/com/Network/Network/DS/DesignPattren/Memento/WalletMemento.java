package com.Network.Network.DS.DesignPattren.Memento;

import java.util.ArrayList;
import java.util.List;

// Memento class to store the state of the wallet (balance and items bought)
public class WalletMemento {
    private int balance;
    private List<String> itemsBought;

    public WalletMemento(int balance, List<String> itemsBought) {
        this.balance = balance;
        this.itemsBought = new ArrayList<>(itemsBought); // Create a copy of the list
    }

    public int getBalance() {
        return balance;
    }

    public List<String> getItemsBought() {
        return itemsBought;
    }
}
