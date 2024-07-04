package com.Network.Network.DS.DesignPattren.CommandDesignPattern;

public class AddItemCommand implements ShoppingCommand {
    private ShoppingCart cart;
    private String item;

    AddItemCommand(ShoppingCart cart, String item) {
        this.cart = cart;
        this.item = item;
    }

    @Override
    public void execute() {
        cart.addItem(item);
    }
}
