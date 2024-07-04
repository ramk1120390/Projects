package com.Network.Network.DS.LinkedList;

public class BinarySearchTree {
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

    public BinarySearchTree() {
        root = null;
    }

    public void insert(int data) {
        Node newNode = new Node(data);
        if (root == null) {
            root = newNode;
            return;
        }
        Node current = root;
        Node parent = null;

        while (true) {
            parent = current;

            if (data < current.data) {
                current = current.left;
                if (current == null) {
                    parent.left = newNode;
                    return; // Exit loop
                }
            } else {
                current = current.right;
                if (current == null) {
                    parent.right = newNode;
                    return; // Exit loop
                }
            }
        }
    }

    public boolean search(int data) {
        Node current = root;
        while (current != null) {
            if (data == current.data) {
                return true;
            } else if (data < current.data) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }
    public int findMax() {
        if (root == null) {
            throw new RuntimeException("Tree is empty");
        }
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.data;
    }
    public int sumBT(Node root) {
        if (root == null)
            return 0;
        int sum = 0;
        sum += sumBT(root.left);
        sum += root.data;
        sum += sumBT(root.right);
        return sum;
    }

    public int findHeight() {
        return findHeight(root);
    }

    private int findHeight(Node node) {
        if (node == null) {
            return -1;
        }
        int leftHeight = findHeight(node.left);
        int rightHeight = findHeight(node.right);
        return Math.max(leftHeight, rightHeight) + 1;
    }
    // Method to check for the Children Sum Property
    public boolean hasChildrenSumProperty() {
        return hasChildrenSumProperty(root);
    }

    // Helper method to check for the Children Sum Property
    private boolean hasChildrenSumProperty(Node node) {
        if (node == null || (node.left == null && node.right == null)) {
            // If node is null or a leaf node, it satisfies the property
            return true;
        }

        int leftData = (node.left != null) ? node.left.data : 0;
        int rightData = (node.right != null) ? node.right.data : 0;

        if (node.data == leftData + rightData &&
                hasChildrenSumProperty(node.left) &&
                hasChildrenSumProperty(node.right)) {
            return true;
        }

        return false;
    }
    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int value : values) {
            bst.insert(value);
        }
        System.out.println("Height of the tree: " + bst.findHeight());
    }
}
