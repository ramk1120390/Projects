package com.Network.Network.DS.DesignPattren.AbstractFactory;

public class AbstractMain {
    public static void main(String[] args) {
        Application application = new Application(new WinUiFactory());
        Application application1 = new Application(new MacUiFactory());
        application.paint();
        application1.paint();
    }
}
