package com.Network.Network.DS.DesignPattren.Flyweight;

public class CapitalizedTextFormat implements TextFormat {
    @Override
    public void format(String text) {
        if (text == null || text.isEmpty()) {
            System.out.println("Empty text cannot be capitalized.");
        } else {
            String firstChar = text.substring(0, 1).toUpperCase();
            String restOfString = text.substring(1).toLowerCase();
            System.out.println("Converting to capitalized: " + firstChar + restOfString);
        }
    }
}