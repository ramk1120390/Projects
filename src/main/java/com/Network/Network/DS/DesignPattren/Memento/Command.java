package com.Network.Network.DS.DesignPattren.Memento;

// Command interface for executing operations on the Wallet object
public interface Command {
    void execute();

    void undo();
}
