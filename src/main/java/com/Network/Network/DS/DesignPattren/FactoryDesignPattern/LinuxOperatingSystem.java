package com.Network.Network.DS.DesignPattren.FactoryDesignPattern;

public class LinuxOperatingSystem extends OperatingSystem {
    public LinuxOperatingSystem(String version, String architecture) {
        super(version, architecture);
    }

    @Override
    public void changeDir(String dir) {
        System.out.println("Changing directory in Linux to: " + dir);
    }

    @Override
    public void removeDir(String dir) {
        System.out.println("Removing directory in Linux: " + dir);
    }

    @Override
    public String toString() {
        return "LinuxOperatingSystem{" +
                "version='" + getVersion() + '\'' +
                ", architecture='" + getArchitecture() + '\'' +
                '}';
    }
}
