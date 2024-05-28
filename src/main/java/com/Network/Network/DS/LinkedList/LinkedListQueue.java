package com.Network.Network.DS.LinkedList;

public class LinkedListQueue {
    class Node {
        int data;
        Node next;
        Node(int val) {
            data = val;
            next = null;
        }
    }
    Node front, rear;
    LinkedListQueue() {
        front = null;
        rear = null;
    }
    public void enque(int data) {
        Node newnode = new Node(data);
        if (rear == null) {
            front = newnode;
            rear=newnode;
        }
        rear.next = newnode;
        rear = newnode;
    }
    public int deque() {
        if (front == null) {
            throw new RuntimeException("Queue is Empty");
        }
        int temp = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        return temp;
    }
    public boolean isempty() {
        return front == null;
    }
    public int peek() {
        if (front == null) {
            throw new RuntimeException("Queue is Empty");
        }
        return front.data;
    }
    public void display() {
        Node temp = front;
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        LinkedListQueue queue=new LinkedListQueue();
        queue.enque(10);
        queue.enque(20);
        queue.display();
        queue.deque();
        queue.display();
        queue.deque();
        queue.peek();
    }
}
