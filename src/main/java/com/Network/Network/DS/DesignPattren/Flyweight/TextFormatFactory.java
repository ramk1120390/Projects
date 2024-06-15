package com.Network.Network.DS.DesignPattren.Flyweight;

import java.util.HashMap;
import java.util.Map;

// Updated Flyweight factory
public class TextFormatFactory {
    private static final Map<String, TextFormat> formats = new HashMap<>();

    public static TextFormat getFormat(String key) {
        TextFormat format = formats.get(key);
        if (format == null) {
            switch (key) {
                case "uppercase":
                    format = new UpperCaseTextFormat();
                    break;
                case "lowercase":
                    format = new LowerCaseTextFormat();
                    break;
                case "capitalize":
                    format = new CapitalizedTextFormat();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown format key: " + key);
            }
            formats.put(key, format);
        }
        return format;
    }
}
