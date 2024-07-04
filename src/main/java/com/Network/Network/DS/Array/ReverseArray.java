package com.Network.Network.DS.Array;

public class ReverseArray {
    public static void main(String[] args) {
        int arr[] = {10, 20, 30, 40, 50};
        int reverse[] = new int[arr.length];

        int n = arr.length;
        for (int i = n - 1, j = 0; i >= 0; i--, j++)
            reverse[j] = arr[i];
        for (int i=0;i< reverse.length;i++)
        {
            System.out.println(reverse[i]);
        }
    }

}

