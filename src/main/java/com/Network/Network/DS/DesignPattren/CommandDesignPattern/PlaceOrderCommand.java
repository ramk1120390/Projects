package com.Network.Network.DS.DesignPattren.CommandDesignPattern;

public class PlaceOrderCommand implements ShoppingCommand {
    private ShoppingCart cart;

    PlaceOrderCommand(ShoppingCart cart) {
        this.cart = cart;
    }

    public void execute() {
        cart.placeOrder();
    }
}
