package com.Network.Network.java8.FunnctinalInterface;

import java.util.List;
import java.util.ArrayList;
import java.util.function.*;
import java.util.stream.Collectors;

public class Funcmain {
    public static void main(String[] args) {
        List<Device> devices = new ArrayList<>();
        devices.add(new Device("1", "Router", "Networking", 120.0));
        devices.add(new Device("2", "Router1", "Networking", 150.0));
        devices.add(new Device("3", "Switch", "Networking", 80.0));
        devices.add(new Device("4", "Laptop", "Computing", 1000.0));
        devices.add(new Device("5", "Desktop", "Computing", 700.0));

        // Function to calculate the total price of a list of devices
        Function<List<Device>, Double> calculateTotalPrice = devicesList ->
                devicesList.stream()
                        .mapToDouble(Device::getPrice)
                        .sum();

        double totalPrice = calculateTotalPrice.apply(devices);
        System.out.println("Total price of all devices: " + totalPrice);

        // Predicate to filter devices by type
        Predicate<Device> isNetworkingDevice = device -> "Networking".equals(device.getType());

        // Predicate to filter devices by price below a certain amount
        Predicate<Device> isPriceBelow200 = device -> device.getPrice() < 200.0;

        // Get all networking devices
        List<Device> networkingDevices = filterDevices(devices, isNetworkingDevice);
        System.out.println("Networking Devices: " + networkingDevices);

        // Get all devices with price below 200
        List<Device> cheapDevices = filterDevices(devices, isPriceBelow200);
        System.out.println("Devices with price below 200: " + cheapDevices);

        // Combine predicates to get networking devices with price below 200
        List<Device> cheapNetworkingDevices = filterDevices(devices, isNetworkingDevice.and(isPriceBelow200));
        System.out.println("Cheap Networking Devices: " + cheapNetworkingDevices);

        // Consumer to print device details
        Consumer<Device> printDevice = System.out::println;
        devices.forEach(printDevice);

        // Supplier to create a new device
        Supplier<Device> newDeviceSupplier = () -> new Device("6", "Access Point", "Networking", 70.0);
        Device newDevice = newDeviceSupplier.get();
        System.out.println("New Device: " + newDevice);

        // BiFunction to update the price of a device
        BiFunction<Device, Double, Device> updatePrice = (device, newPrice) -> {
            device.setPrice(newPrice);
            return device;
        };
        Device updatedDevice = updatePrice.apply(devices.get(0), 130.0);
        System.out.println("Updated Device: " + updatedDevice);

        // BiConsumer to set the name of a device
        BiConsumer<Device, String> setName = Device::setName;
        setName.accept(devices.get(1), "Updated Router1");
        System.out.println("Updated Device Name: " + devices.get(1));

        // BiPredicate to check if the price of a device is below a certain amount
        BiPredicate<Device, Double> isPriceBelow = (device, price) -> device.getPrice() < price;
        devices.forEach(device -> System.out.println("Is price below 200: " + isPriceBelow.test(device, 200.0)));

        // UnaryOperator to increase the price of a device
        UnaryOperator<Device> increasePrice = device -> {
            device.setPrice(device.getPrice() * 1.1); // Increase price by 10%
            return device;
        };
        Device increasedPriceDevice = increasePrice.apply(devices.get(2));
        System.out.println("Increased Price Device: " + increasedPriceDevice);

        // BinaryOperator to find the more expensive device
        BinaryOperator<Device> moreExpensiveDevice = (d1, d2) -> d1.getPrice() > d2.getPrice() ? d1 : d2;
        Device moreExpensive = moreExpensiveDevice.apply(devices.get(3), devices.get(4));
        System.out.println("More Expensive Device: " + moreExpensive);
    }

    // Method to filter devices based on a predicate
    public static List<Device> filterDevices(List<Device> devices, Predicate<Device> predicate) {
        return devices.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
