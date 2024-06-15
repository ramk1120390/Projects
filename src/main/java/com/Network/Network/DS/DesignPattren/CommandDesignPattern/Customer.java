package com.Network.Network.DS.DesignPattren.CommandDesignPattern;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private List<ShoppingCommand> commands = new ArrayList<>();

    void addCommand(ShoppingCommand command) {
        commands.add(command);
    }

    void executeCommands() {
        for (ShoppingCommand command : commands) {
            command.execute();
        }
        commands.clear();
    }
}

