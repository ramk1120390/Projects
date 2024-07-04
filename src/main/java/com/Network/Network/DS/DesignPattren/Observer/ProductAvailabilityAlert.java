package com.Network.Network.DS.DesignPattren.Observer;

public class ProductAvailabilityAlert {
    public static void main(String[] args) {
        ProductManager manager = new ProductManager();

        User user1 = new User("User 1");
        User user2 = new User("User 2");
        User user3 = new User("User 3");

        manager.attach(user1);
        manager.attach(user2);

        // Adding new products
        Product product1 = new Product("T-Shirt", "Out of Stock");
        Product product2 = new Product("Jeans", "In Stock");

        manager.addProduct(product1); // Notify all attached users
        manager.addProduct(product2); // Notify all attached users

        // User 3 subscribes for updates
        manager.attach(user3);

        // Updating product availability
        manager.updateProductAvailability("T-Shirt", "In Stock"); // Notify all attached users

        // User 2 unsubscribes from updates
        manager.detach(user2);

        // Updating product availability
        manager.updateProductAvailability("Jeans", "Out of Stock"); // Notify remaining attached users

        // New user subscribes and is notified about specific products
        User user4 = new User("User 4");
        manager.attach(user4);
        manager.notifySpecificObserver(product1, user4);
        manager.notifySpecificObserver(product2, user4);
    }
}