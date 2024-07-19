package com.Network.Network.DS.LinkedList;

public class StackLL {
    class Node {
        int data;
        Node next;

        Node(int val) {
            data = val;
            next = null;
        }
    }

    Node top;

    StackLL() {
        top = null;
    }

    public void push(int data) {
        Node nn = new Node(data);
        if (top == null) {
            top = nn;
        } else {
            nn.next = top;
            top = nn;
        }
    }

    public int pop() {
        if (top == null) {
            throw new RuntimeException("Stack is Empty");
        }
        int temp = top.data;
        top = top.next;
        return temp;
    }

    public int peek() {
        if (top == null) {
            throw new RuntimeException("Stack is Empty");
        }
        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }
    public void display() {
        if (top == null) {
            System.out.println("Stack is Empty");
            return;
        }
        Node current = top;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }
    public static void main(String[] args) {
        StackLL stack = new StackLL();
        stack.push(10);
        stack.push(20);
        stack.push(30);
        stack.display();

        System.out.println(stack.pop()); // 30
        System.out.println(stack.peek()); // 20
        System.out.println(stack.pop()); // 20
        stack.display();
        System.out.println(stack.pop()); // 10
    }
}
