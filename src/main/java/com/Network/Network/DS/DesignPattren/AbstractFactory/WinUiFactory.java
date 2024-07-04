package com.Network.Network.DS.DesignPattren.AbstractFactory;

public class WinUiFactory implements UIFactory {
    @Override
    public Button createButton() {
        return new WinButton();
    }

    @Override
    public CheckBox createCheckBox() {
        return new WinCheckBox();
    }
}
