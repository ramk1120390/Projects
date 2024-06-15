package com.Network.Network.DS.DesignPattren.FactoryDesignPattern;

public class FactoryMain {
    public static void main(String[] args) {
        OperatingSystem windowsOS = OperatingSystemFactory.getInstance("WINDOWS", "WIN7", "x64");
        OperatingSystem linuxOS = OperatingSystemFactory.getInstance("LINUX", "DEB", "x64");

        System.out.println(windowsOS);
        System.out.println(linuxOS);
        linuxOS.changeDir("/home/user");
        linuxOS.removeDir("/home/user/documents");

        windowsOS.changeDir("C:\\Users");
        windowsOS.removeDir("C:\\Users\\Documents");

    }
}