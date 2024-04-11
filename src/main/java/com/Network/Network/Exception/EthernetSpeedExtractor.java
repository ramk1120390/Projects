package com.Network.Network.Exception;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EthernetSpeedExtractor {
    public static void main(String[] args) {
        // Sample JSON data
        String bandwidth = "20000000 Kbit";
        String speed = "20000Mb/s";
        int speedvalue = 0;
        String speedunits = "NA";

      // String bandwidth = vlanInterface.path("bandwidth").asText();
       // String speed = vlanInterface.path("speed").asText();

// Check if "speed" is not null and not empty
        if (speed != null && !speed.isEmpty()) {
            // If "speed" is not empty, use "speed" data
            Pattern speedPattern = Pattern.compile("(\\d+)\\s*(\\w+)/(\\w+)");
            Matcher speedMatcher = speedPattern.matcher(speed);
            if (speedMatcher.matches()) {
                speedvalue = Integer.parseInt(speedMatcher.group(1));
                speedunits = speedMatcher.group(2).toLowerCase();
                String suffix = speedMatcher.group(3);
                if (suffix.equalsIgnoreCase("s")) {
                    speedunits += "/" + suffix;
                }
                //ethernet.setCapacityValue(speedvalue);
                //ethernet.setCapacityUnits(speedunits);
                System.out.println("Speed: " + speedvalue + " " + speedunits);
            }
        } else {
            // If "speed" is empty and "bandwidth" is not empty, use "bandwidth" data
            Pattern bandwidthPattern = Pattern.compile("(\\d+)\\s*(\\w+)");
            Matcher bandwidthMatcher = bandwidthPattern.matcher(bandwidth);
            if (bandwidthMatcher.matches()) {
                speedvalue = Integer.parseInt(bandwidthMatcher.group(1));
                speedunits = bandwidthMatcher.group(2).toLowerCase();
                //ethernet.setCapacityValue(speedvalue);
                //ethernet.setCapacityUnits(speedunits);
                System.out.println("Bandwidth: " + speedvalue + " " + speedunits);
            }
        }
    }
}