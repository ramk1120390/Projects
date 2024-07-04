package com.Network.Network.DS.DesignPattren.Memento;

// Main class to demonstrate Wallet-based Shopping with Undo/Redo using Command and Memento pattern
public class WalletExample {
    public static void main(String[] args) {
        // Create Wallet object and WalletHistory caretaker
        Wallet wallet = new Wallet(1000); // Starting balance of 1000
        WalletHistory history = new WalletHistory();

        // Initial state of the wallet
        history.saveMemento(wallet.saveToMemento());

        // Execute commands (buy items)
        Command buyItemCommand1 = new BuyItemCommand(wallet, "Item1", 500);
        history.executeCommand(buyItemCommand1);
        history.saveMemento(wallet.saveToMemento());

        Command buyItemCommand2 = new BuyItemCommand(wallet, "Item2", 800);
        history.executeCommand(buyItemCommand2);
        history.saveMemento(wallet.saveToMemento());

        // Print current items in the wallet and available balance
        System.out.println("Current Items in Wallet: " + wallet.getItemsBought());
        System.out.println("Available Balance: " + wallet.getBalance());

        // Undo last action
        history.undoCommand();
        System.out.println("After Undo: " + wallet.getItemsBought());
        System.out.println("Available Balance after Undo: " + wallet.getBalance());

        // Undo to last saved state
        history.undoToLastSavedMemento();
        System.out.println("After Undo to Last Saved Memento: " + wallet.getItemsBought());
        System.out.println("Available Balance after Undo to Last Saved Memento: " + wallet.getBalance());
    }
}
