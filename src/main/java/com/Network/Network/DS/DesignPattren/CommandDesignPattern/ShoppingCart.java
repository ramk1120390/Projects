package com.Network.Network.DS.DesignPattren.CommandDesignPattern;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<String> items = new ArrayList<>();

    void addItem(String item) {
        items.add(item);
        System.out.println(item + " added to the cart.");
    }

    void removeItem(String item) {
        if (items.contains(item)) {
            items.remove(item);
            System.out.println(item + " removed from the cart.");
        } else {
            System.out.println(item + " not found in the cart.");
        }
    }

    void placeOrder() {
        System.out.println("Order placed for items in the cart: " + items);
        items.clear();
    }
}
