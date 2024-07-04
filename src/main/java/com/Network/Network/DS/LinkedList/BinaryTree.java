package com.Network.Network.DS.LinkedList;

public class BinaryTree {
    Node root;

    class Node {
        int data;
        Node left, right;

        public Node(int data) {
            this.data = data;
            left = null;
            right = null;
        }
    }

    public BinaryTree(int data) {
        root = new Node(data);
    }

    public void insertleft(Node r, int val) {
        Node node = new Node(val);
        r.left = node;
    }

    public void insertright(Node r, int val) {
        Node node = new Node(val);
        r.right = node;
    }

    public static void preorder(Node root) {
        if (root != null) {
            System.out.println(root.data);
            preorder(root.left);
            preorder(root.right);
        }
    }
    public static void inorder(Node root) {
        if (root != null) {
            inorder(root.left);
            System.out.println(root.data);
            inorder(root.right);
        }
    }
    public static void postorder(Node root) {
        if (root != null) {
            postorder(root.left);
            postorder(root.right);
            System.out.println(root.data);
        }
    }

    public static void main(String[] args) {
        BinaryTree binaryTree = new BinaryTree(10);
        binaryTree.insertleft(binaryTree.root, 15);
        binaryTree.insertright(binaryTree.root, 20);
        binaryTree.insertleft(binaryTree.root.left, 30);
        System.out.println ("Preorder traversal");
        binaryTree.preorder (binaryTree.root);

        System.out.println ("\nInorder traversal");
        binaryTree.inorder (binaryTree.root);

        System.out.println ("\nPostorder traversal");
        binaryTree.postorder (binaryTree.root);

    }
}

