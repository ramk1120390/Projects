package com.Network.Network.DS.LinkedList;

public class Stack {
    static int[] s = new int[6];
    int top;

    Stack() {
        top = -1;
    }

    void push(int item) {
        if (top == s.length) {
            throw new RuntimeException("Stack is full");
        }
        s[++top] = item;
        System.out.println (item + " pushed into stack");
    }

    int pop() {
        if (top >= 0) {
            return s[top--];
        } else {
            System.out.println("Stack empty");
            return -1;
        }
    }

    public static void main(String[] args) {
        Stack stack=new Stack();
        stack.push(12);
        stack.push(13);
        stack.push(14);
        System.out.println ("element poped out : " + stack.pop ());
        for (int value : s) {
            System.out.println(value);
        }
    }

}
