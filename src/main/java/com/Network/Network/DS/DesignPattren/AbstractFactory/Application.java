package com.Network.Network.DS.DesignPattren.AbstractFactory;

public class Application {
    private Button button;
    private CheckBox checkBox;

    public Application(UIFactory uiFactory) {
        button = uiFactory.createButton();
        checkBox = uiFactory.createCheckBox();
    }

    public void paint() {
        button.paint();
        checkBox.paint();
    }
}
