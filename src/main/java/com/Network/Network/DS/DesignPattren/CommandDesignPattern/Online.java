package com.Network.Network.DS.DesignPattren.CommandDesignPattern;

public class Online {
    public static void main(String[] args) {
        // Create the Receiver
        ShoppingCart cart = new ShoppingCart();

        // Create the Concrete Command objects with the associated Receiver
        AddItemCommand addItemCommand1 = new AddItemCommand(cart, "Shirt");
        AddItemCommand addItemCommand2 = new AddItemCommand(cart, "Jeans");
        RemoveItemCommand removeItemCommand = new RemoveItemCommand(cart, "Shirt");
        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(cart);

        // Create the Invoker
        Customer customer = new Customer();

        // Customer adds items to the cart
        customer.addCommand(addItemCommand1);
        customer.addCommand(addItemCommand2);

        // Customer removes an item from the cart
        customer.addCommand(removeItemCommand);

        // Customer places the order
        customer.addCommand(placeOrderCommand);

        // Customer executes all the commands
        customer.executeCommands();
    }
}
