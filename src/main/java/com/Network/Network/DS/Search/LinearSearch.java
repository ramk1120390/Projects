package com.Network.Network.DS.Search;

public class LinearSearch {
    public static int search(int[] Arr, int num) {
        int foundIndex = -1; // Initialize foundIndex to -1
        for (int i = 0; i < Arr.length; i++) {
            if (Arr[i] == num) {
                foundIndex = i; // Update foundIndex if num is found
                break; // Exit the loop once num is found
            }
        }
        return foundIndex;
    }

    public static void main(String args[]) {
        int[] arr = {12, 5, 18, 25, -3, 19};
        int item = 25;
        int Ans = search(arr, item);
        System.out.println(Ans);
    }
}
