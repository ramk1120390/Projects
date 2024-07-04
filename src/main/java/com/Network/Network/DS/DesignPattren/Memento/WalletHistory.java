package com.Network.Network.DS.DesignPattren.Memento;

// Caretaker class that manages commands and mementos

import java.util.Stack;

public class WalletHistory {
    private Stack<Command> commands = new Stack<>();
    private Stack<WalletMemento> mementos = new Stack<>();

    public void executeCommand(Command command) {
        commands.push(command);
        command.execute();
    }

    public void undoCommand() {
        if (!commands.isEmpty()) {
            Command command = commands.pop();
            command.undo();
        }
    }

    public void saveMemento(WalletMemento memento) {
        mementos.push(memento);
    }

    public WalletMemento undoToLastSavedMemento() {
        if (!mementos.isEmpty()) {
            return mementos.pop();
        }
        return null;
    }
}
