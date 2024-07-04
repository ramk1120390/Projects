package com.Network.Network.DS.DesignPattren.FacadeDesign;

import java.util.List;

public class SwiggyFacade {
    private RestaurantSearch restaurantSearch;
    private OrderPlacement orderPlacement;
    private PaymentProcessing paymentProcessing;

    public SwiggyFacade() {
        this.restaurantSearch = new RestaurantSearch();
        this.orderPlacement = new OrderPlacement();
        this.paymentProcessing = new PaymentProcessing();
    }

    public void orderFood(String location, String restaurant, String item, String paymentMethod) {
        List<String> restaurants = restaurantSearch.searchRestaurants(location);
        if (restaurants.contains(restaurant)) {
            orderPlacement.placeOrder(restaurant, item);
            paymentProcessing.processPayment(paymentMethod);
            System.out.println("Order placed successfully at " + restaurant + " for " + item + " with payment method " + paymentMethod + ".");
        } else {
            System.out.println("Restaurant not found in the given location.");
        }
    }
}