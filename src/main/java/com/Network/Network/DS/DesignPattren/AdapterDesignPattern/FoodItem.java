package com.Network.Network.DS.DesignPattren.AdapterDesignPattern;

public class FoodItem implements Item {
    private String itemName;
    private String price;
    private String restaurantName;

    public FoodItem(String itemName, String price, String restaurantName) {
        this.itemName = itemName;
        this.price = price;
        this.restaurantName = restaurantName;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    @Override
    public String getPrice() {
        return price;
    }

    @Override
    public String getRestaurantName() {
        return restaurantName;
    }
}