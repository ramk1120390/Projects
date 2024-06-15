package com.Network.Network.DS.DesignPattren.Flyweight;

public class UpperCaseTextFormat implements TextFormat {
    @Override
    public void format(String text) {
        System.out.println("Converting to uppercase: " + text.toUpperCase());
    }
}