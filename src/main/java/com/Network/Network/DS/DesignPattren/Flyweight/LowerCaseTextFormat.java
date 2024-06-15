package com.Network.Network.DS.DesignPattren.Flyweight;

public class LowerCaseTextFormat implements TextFormat {
    @Override
    public void format(String text) {
        System.out.println("Converting to lowercase: " + text.toLowerCase());
    }
}