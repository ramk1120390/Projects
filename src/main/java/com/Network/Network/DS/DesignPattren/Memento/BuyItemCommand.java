package com.Network.Network.DS.DesignPattren.Memento;

// Concrete command to buy an item
public class BuyItemCommand implements Command {
    private Wallet wallet;
    private String item;
    private int itemPrice;

    public BuyItemCommand(Wallet wallet, String item, int itemPrice) {
        this.wallet = wallet;
        this.item = item;
        this.itemPrice = itemPrice;
    }

    @Override
    public void execute() {
        if (wallet.getBalance() >= itemPrice) {
            wallet.addItemBought(item);
            wallet.balance -= itemPrice;
        } else {
            System.out.println("Insufficient balance to buy " + item);
        }
    }

    @Override
    public void undo() {
        wallet.removeItemBought(item);
        wallet.balance += itemPrice;
    }
}
