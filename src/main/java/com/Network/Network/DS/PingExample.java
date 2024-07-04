package com.Network.Network.DS;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class PingExample {
    public static void main(String[] args) throws UnknownHostException, IOException {
        String ipAddress = "192.168.0.1"; // Replace with the IP address you want to ping
        InetAddress inet = InetAddress.getByName(ipAddress);
        System.out.println("Sending Ping Request to " + ipAddress);
        boolean isReachable = inet.isReachable(5000); // Timeout = 5000 milliseconds
        System.out.println(isReachable ? "Host is reachable" : "Host is NOT reachable");
    }
}
