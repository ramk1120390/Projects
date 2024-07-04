package com.Network.Network.DS.DesignPattren.Observer;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private List<Observer> observers = new ArrayList<>();
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
        notifyObservers(product);
    }

    public void updateProductAvailability(String productName, String availability) {
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                product.setAvailability(availability);
                notifyObservers(product);
            }
        }
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers(Product product) {
        for (Observer observer : observers) {
            observer.update(product);
        }
    }

    public void notifySpecificObserver(Product product, Observer observer) {
        observer.update(product);
    }
}
