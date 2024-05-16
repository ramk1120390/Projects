package com.Network.Network.DS.Search;

import java.util.Arrays;

public class BinarySearch {
    public static int search(int arr[], int search) {
        Arrays.sort(arr);
        int left = 0;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == search)
                return mid;
            if (arr[mid] < search)
                left = mid + 1;
            else
                right = mid - 1;
        }
        return -1;
    }

    public static void main(String args[]) {
        int[] arr = {12, 5, 18, 25, -3, 19};
        int item = 25;
        int Ans = search(arr, item);
        System.out.println(Ans);
    }
}

