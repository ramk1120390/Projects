package com.Network.Network.DS.LinkedList;

public class LinkedList {
    Node head;

    class Node {
        int data;
        Node next;

        Node(int val) {
            data = val;
            next = null;
        }
    }

    LinkedList() {
        head = null;
    }

    public void InsertBegin(int data) {
        Node newnode = new Node(data);
        newnode.next = head;
        head = newnode;
    }

    public void InsertEnd(int data) {
        Node newnode = new Node(data);
        if (head == null) {
            head = newnode;
            return;
        }
        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = newnode;
    }

    public void insertAtPos(int pos, int data) {
        if (pos < 0) {
            throw new IllegalArgumentException("Position can't be negative");
        }
        if (pos == 0) {
            InsertBegin(data);
            return;
        }
        Node newnode = new Node(data);
        Node temp = head;
        for (int i = 1; i < pos; i++) {
            if (temp == null || temp.next == null) {
                throw new IllegalArgumentException("Given position not found");
            }
            temp = temp.next;
        }
        newnode.next = temp.next;
        temp.next = newnode;
    }

    public void deleteAtPost(int pos) {
        if (head==null)
        {
            throw new ArrayIndexOutOfBoundsException("Given list empty");
        }
        if (pos==0)
        {
            head=head.next;
            return;
        }
        Node temp = head;
        Node prev = null;
        for (int i = 1; i < pos; i++) {
            prev = temp;
            temp = temp.next;
        }
        prev.next = temp.next;
    }

    public void display() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.println();
    }
    public void reverse() {
        Node previous = null;
        Node current = head;
        Node next = null;
        while (current != null) {
            next = current.next;  // Store the next node
            current.next = previous;  // Reverse the current node's pointer
            previous = current;  // Move pointers one position ahead
            current = next;
        }
        head = previous;  // Reset the head to the new front of the list
    }

    public static void main(String[] args) {
        LinkedList lc = new LinkedList();
        lc.InsertBegin(1);
        lc.InsertBegin(2);
        lc.InsertBegin(3);
        lc.display(); // Should print: 3 2 1

        lc.InsertEnd(5);
        lc.display(); // Should print: 3 2 1 5

        lc.insertAtPos(2, 10);
        lc.display(); // Should print: 3 2 10 1 5

        try {
            lc.insertAtPos(10, 12); // This will throw an exception
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        lc.deleteAtPost(3);
        lc.display();
        lc.deleteAtPost(0);
        lc.display();
    }
}