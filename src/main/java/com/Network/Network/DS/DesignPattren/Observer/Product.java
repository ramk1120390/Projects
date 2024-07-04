package com.Network.Network.DS.DesignPattren.Observer;

public class Product {
    private String name;
    private String availability;

    public Product(String name, String availability) {
        this.name = name;
        this.availability = availability;
    }

    public String getName() {
        return name;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}