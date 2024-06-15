package com.Network.Network.DS.DesignPattren.AdapterDesignPattern;

public class AdapterMain {
    public static void main(String[] args) {
        SwiggyStore swiggyStore = new SwiggyStore();

        // Create food items with specific details
        FoodItem foodItem1 = new FoodItem("Burger", "100", "McDonald's");
        FoodItem foodItem2 = new FoodItem("Pizza", "200", "Domino's");

        // Add food items to the Swiggy store
        swiggyStore.addItems(foodItem1);
        swiggyStore.addItems(foodItem2);

        // Create grocery item with specific details
        GroceryProduct groceryItem = new GroceryProduct("Milk", "50", "Supermarket");

        // Adapter for GroceryItem and add it to the Swiggy store
        swiggyStore.addItems(new GroceryItemAdapter(groceryItem));

        // Print the items in the Swiggy store
        swiggyStore.printItems();
    }
}
