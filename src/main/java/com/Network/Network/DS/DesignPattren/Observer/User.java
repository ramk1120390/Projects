package com.Network.Network.DS.DesignPattren.Observer;

public class User implements Observer {
    private String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public void update(Product product) {
        System.out.println(name + " received update! Product: " + product.getName() + " Availability: " + product.getAvailability());
    }
}