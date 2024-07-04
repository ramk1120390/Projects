package com.Network.Network.DS.DesignPattren.Flyweight;

// Updated Client code
public class TextEditor {
    public void formatText(String text, String formatType) {
        TextFormat format = TextFormatFactory.getFormat(formatType);
        format.format(text);
    }
}
