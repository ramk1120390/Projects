package com.Network.Network.DS.DesignPattren.FacadeDesign;

import java.util.Arrays;
import java.util.List;

public class RestaurantSearch {
    public List<String> searchRestaurants(String location) {
        System.out.println("Searching restaurants in " + location + "...");
        return Arrays.asList("Restaurant A", "Restaurant B", "Restaurant C");
    }
}