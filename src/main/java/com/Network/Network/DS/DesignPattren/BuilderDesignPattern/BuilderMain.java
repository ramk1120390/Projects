package com.Network.Network.DS.DesignPattren.BuilderDesignPattern;

import java.util.ArrayList;
import java.util.List;

public class BuilderMain {
    public static void main(String[] args) {
        // List of interface configurations
        List<InterfaceConfig> configs = new ArrayList<>();
        configs.add(new InterfaceConfig("Cisco", "Loopback1", "Tunnel1", "Sw1", "Remote1", "BridgeDomain1", "Vlan1"));
        configs.add(new InterfaceConfig("Ciena", "Loopback2", "Tunnel2", "Sw2", "Remote2", "BridgeDomain2", "Vlan2"));

        // List to store created interfaces
        List<InterFaces> interfacesList = new ArrayList<>();

        // Build interfaces using configurations
        for (InterfaceConfig config : configs) {
            InterFaceBuilder builder = getBuilder(config.getVendor(), config);
            InterFaceDirector director = new InterFaceDirector(builder);
            //  CienaInterfaceBuilder director1=new CienaInterfaceBuilder(config);
            InterFaces interFaces = director.makeInterFace();
            interfacesList.add(interFaces);
        }

        // Print the details of each interface
        for (InterFaces interFaces : interfacesList) {
            System.out.println(interFaces);
        }
    }

    private static InterFaceBuilder getBuilder(String vendor, InterfaceConfig config) {
        switch (vendor) {
            case "Cisco":
                return new CiscoInterFaceBuilder(config);
            case "Ciena":
                return new CienaInterfaceBuilder(config);
            default:
                throw new IllegalArgumentException("Unknown vendor: " + vendor);
        }
    }
}
