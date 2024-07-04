package com.Network.Network.DS.DesignPattren.FactoryDesignPattern;

public class WindowsOperatingSystem extends OperatingSystem {
    public WindowsOperatingSystem(String version, String architecture) {
        super(version, architecture);
    }

    @Override
    public void changeDir(String dir) {
        System.out.println("Changing directory in Windows to: " + dir);
    }

    @Override
    public void removeDir(String dir) {
        System.out.println("Removing directory in Windows to: " + dir);
    }

    @Override
    public String toString() {
        return "WindowsOperatingSystem{" +
                "version='" + getVersion() + '\'' +
                ", architecture='" + getArchitecture() + '\'' +
                '}';
    }
}
