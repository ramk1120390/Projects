package com.Network.Network.DS.DesignPattren.Memento;

import java.util.ArrayList;
import java.util.List;

// Originator class (Wallet) that maintains the balance and items bought, and creates mementos
public class Wallet {
    int balance;
    private List<String> itemsBought;

    public Wallet(int balance) {
        this.balance = balance;
        this.itemsBought = new ArrayList<>();
    }

    public int getBalance() {
        return balance;
    }

    public void addItemBought(String item) {
        itemsBought.add(item);
    }

    public void removeItemBought(String item) {
        itemsBought.remove(item);
    }

    public List<String> getItemsBought() {
        return itemsBought;
    }

    public WalletMemento saveToMemento() {
        return new WalletMemento(balance, itemsBought);
    }

    public void restoreFromMemento(WalletMemento memento) {
        this.balance = memento.getBalance();
        this.itemsBought = new ArrayList<>(memento.getItemsBought()); // Restore from a copy of the list
    }
}
