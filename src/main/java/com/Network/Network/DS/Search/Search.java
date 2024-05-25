package com.Network.Network.DS.Search;

import java.util.Arrays;

public class Search {
    public static int binarySearch(int arr[], int search) {
        Arrays.sort(arr);
        int left = 0;
        int right = arr.length - 1;
        int steps = 0; // Initialize steps counter
        while (left <= right) {
            steps++;
            int mid = (left+right)/2;
            if (arr[mid] == search) {
                System.out.println("Binary Search: Steps taken: " + steps);
                return mid;
            }
            if (arr[mid] < search)
                left = mid + 1;
            else
                right = mid - 1;
        }
        System.out.println("Binary Search: Steps taken: " + steps);
        return -1;
    }

    public static int linearSearch(int[] Arr, int num) {
        int steps = 0;
        int foundIndex = -1;
        for (int i = 0; i < Arr.length; i++) {
            steps++;
            if (Arr[i] == num) {
                foundIndex = i;
                break;
            }
        }
        if (foundIndex != -1) {
            System.out.println("Linear Search: Steps taken: " + steps); // Print steps count if element found
        } else {
            System.out.println("Linear Search: Element not found after " + steps + " steps."); // Print steps count if element not found
        }
        return foundIndex;
    }

    public static void main(String args[]) {
        int[] arr = {12, 5, 18, 25, -3, 19};
        int item = 25;
        //int[]arr=new int[];
        // Binary Search
        int binaryResult = binarySearch(arr, item);
        if (binaryResult != -1) {
            System.out.println("Binary Search: Element found at index " + binaryResult);
        } else {
            System.out.println("Binary Search: Element not found");
        }

        // Linear Search
        int linearResult = linearSearch(arr, item);
        if (linearResult != -1) {
            System.out.println("Linear Search: Element found at index " + linearResult);
        } else {
            System.out.println("Linear Search: Element not found");
        }
    }
}
