package com.Network.Network.DS.DesignPattren.CommandDesignPattern;

public class RemoveItemCommand implements ShoppingCommand {
    private ShoppingCart cart;
    private String item;

    RemoveItemCommand(ShoppingCart cart, String item) {
        this.cart = cart;
        this.item = item;
    }

    public void execute() {
        cart.removeItem(item);
    }
}