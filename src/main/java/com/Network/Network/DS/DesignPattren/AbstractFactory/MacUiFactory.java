package com.Network.Network.DS.DesignPattren.AbstractFactory;

public class MacUiFactory implements UIFactory {
    @Override
    public Button createButton() {
        return new MacButton();
    }

    @Override
    public CheckBox createCheckBox() {
        return new MacCheckBox();
    }
}
