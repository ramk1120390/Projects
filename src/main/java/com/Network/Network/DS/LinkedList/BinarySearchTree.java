package com.Network.Network.DS.LinkedList;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

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
            System.out.println("Inserted " + data + " as the root.");
            return;
        }

        Node current = root;
        while (true) {
            System.out.println("Current node: " + current.data);
            if (data < current.data) {
                if (current.left == null) {
                    current.left = newNode;
                    System.out.println("Inserted " + data + " as the left child of " + current.data);
                    break;
                } else {
                    current = current.left;
                    System.out.println("Moving to left child: " + current.data);
                }
            } else {
                if (current.right == null) {
                    current.right = newNode;
                    System.out.println("Inserted " + data + " as the right child of " + current.data);
                    break;
                } else {
                    current = current.right;
                    System.out.println("Moving to right child: " + current.data);
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

    public int sumBT() {
        if (root == null)
            return 0;

        int sum = 0;
        Stack<Node> stack = new Stack<>();
        Node current = root;

        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                System.out.println("Pushed node onto stack: " + current.data); // Debug output
                current = current.left;
            }
            current = stack.pop();
            System.out.println("Popped node from stack: " + current.data); // Debug output
            sum += current.data;
            System.out.println("Current sum: " + sum); // Debug output
            current = current.right;
        }

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

    public boolean hasChildrenSumProperty() {
        if (root == null || (root.left == null && root.right == null)) {
            return true;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            System.out.println(queue.poll());

            int leftData = (node.left != null) ? node.left.data : 0;
            int rightData = (node.right != null) ? node.right.data : 0;

            if (node.data != leftData + rightData) {
                return false;
            }

            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }

        return true;
    }
    public void preorder(Node node) {
        if (node != null) {
            System.out.print(node.data + " ");  // Visit the root
            preorder(node.left);                // Traverse left subtree
            preorder(node.right);               // Traverse right subtree
        }
    }

    public void inorder(Node node) {
        if (node != null) {
            inorder(node.left);                 // Traverse left subtree
            System.out.print(node.data + " ");  // Visit the root
            inorder(node.right);                // Traverse right subtree
        }
    }

    public void postorder(Node node) {
        if (node != null) {
            postorder(node.left);               // Traverse left subtree
            postorder(node.right);              // Traverse right subtree
            System.out.print(node.data + " ");  // Visit the root
        }
    }

    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int value : values) {
            bst.insert(value);
        }

        System.out.println("Sum of all nodes in the tree: " + bst.sumBT());
        System.out.println("Maximum value in the tree: " + bst.findMax());
        System.out.println("Height of the tree: " + bst.findHeight());
        System.out.println("Tree satisfies children sum property: " + bst.hasChildrenSumProperty());

        System.out.println("Preorder traversal: ");
        bst.preorder(bst.root);
        System.out.println();

        System.out.println("Inorder traversal: ");
        bst.inorder(bst.root);
        System.out.println();

        System.out.println("Postorder traversal: ");
        bst.postorder(bst.root);
        System.out.println();
    }
}

///Initial State:
//--------------
//BinarySearchTree:
//    root -> 50
//             / \
//            30  70
//           / \  / \
//          20 40 60 80
//
//Stack (empty):
//--------------
//
//sum = 0
//
//Execution Steps:
//----------------
//1. Start with current = root (50)
//   - Push current (50) onto stack.
//     Stack: [50]
//     sum = 0
//
//2. Move left: current = current.left (30)
//   - Push current (30) onto stack.
//     Stack: [50, 30]
//     sum = 0
//
//3. Move left: current = current.left (20)
//   - Push current (20) onto stack.
//     Stack: [50, 30, 20]
//     sum = 0
//
//4. No left child: pop current (20) from stack.
//   - Add current.data (20) to sum.
//     Stack: [50, 30]
//     sum = 20
//
//5. Move right: current = current.right (40)
//   - Push current (40) onto stack.
//     Stack: [50, 30, 40]
//     sum = 20
//
//6. No left child: pop current (40) from stack.
//   - Add current.data (40) to sum.
//     Stack: [50, 30]
//     sum = 60
//
//7. No left child: pop current (30) from stack.
//   - Add current.data (30) to sum.
//     Stack: [50]
//     sum = 90
//
//8. Move right: current = current.right (70)
//   - Push current (70) onto stack.
//     Stack: [50, 70]
//     sum = 90
//
//9. Move left: current = current.left (60)
//   - Push current (60) onto stack.
//     Stack: [50, 70, 60]
//     sum = 90
//
//10. No left child: pop current (60) from stack.
//    - Add current.data (60) to sum.
//      Stack: [50, 70]
//      sum = 150
//
//11. Move right: current = current.right (80)
//    - Push current (80) onto stack.
//      Stack: [50, 70, 80]
//      sum = 150
//
//12. No left child: pop current (80) from stack.
//    - Add current.data (80) to sum.
//      Stack: [50, 70]
//      sum = 230
//
//13. No left child: pop current (70) from stack.
//    - Add current.data (70) to sum.
//      Stack: [50]
//      sum = 300
//
//14. No left child: pop current (50) from stack.
//    - Add current.data (50) to sum.
//      Stack: []
//      sum = 350
//
//Final State:
//------------
//sum = 350