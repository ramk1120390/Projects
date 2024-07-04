package com.Network.Network.DS.DesignPattren.AdapterDesignPattern;

import java.util.ArrayList;
import java.util.List;

public class SwiggyStore {
    List<Item> items = new ArrayList<>();

    public void addItems(Item item) {
        items.add(item);
    }

    public void printItems() {
        System.out.println("Items in Swiggy Store:");
        for (Item item : items) {
            System.out.println("Item Name: " + item.getItemName());
            System.out.println("Price: " + item.getPrice());
            System.out.println("Restaurant/Store Name: " + item.getRestaurantName());
            System.out.println("--------------------------------------");
        }
    }
}
