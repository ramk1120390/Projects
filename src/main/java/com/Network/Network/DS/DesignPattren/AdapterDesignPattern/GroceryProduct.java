package com.Network.Network.DS.DesignPattren.AdapterDesignPattern;

public class GroceryProduct implements GroceryItem {
    private String name;
    private String price;
    private String storeName;

    public GroceryProduct(String name, String price, String storeName) {
        this.name = name;
        this.price = price;
        this.storeName = storeName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPrice() {
        return price;
    }

    @Override
    public String getStoreName() {
        return storeName;
    }
}
