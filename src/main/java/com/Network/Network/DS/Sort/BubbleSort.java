package com.Network.Network.DS.Sort;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.sort;
import static java.util.Collections.swap;

public class BubbleSort {

    public static void bubbleSort(int[] array) {
        int n = array.length;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;

            // Perform a single pass of bubble sort
            for (int j = 0; j < n - 1 - i; j++) {
                // Compare adjacent elements
                if (array[j] > array[j + 1]) {
                    // Swap if they are in the wrong order
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapped = true;
                }
            }


            // If no elements were swapped during the inner loop, then the list is sorted
            if (!swapped) {
                break;
            }
        }
    }
    public static void insertionSort(int[] array) {
        int n = array.length;

        for (int i = 1; i < n; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    public static void selectionSort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            int minNumber = Arrays.stream(array, i, n).min().getAsInt(); // Using Arrays.stream() with range
            for (int j = i; j < n; j++) {
                if (array[j] == minNumber) {
                    minIndex = j; // Find the index of the minimum number
                    break; // Exit the loop once the index is found
                }
            }
            List<Integer> list = new ArrayList<>();
            for (int num : array) {
                list.add(num);
            }
            swap(list,i,minIndex);
        }
    }
    public static void main(String[] args) {
        int[] array = {64, 34, 25, 12, 22, 11, 90};

        System.out.println("Unsorted array:");
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();

        bubbleSort(array);
        System.out.println("Sorted array:");
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();
        insertionSort(array) ;
        System.out.println("Sorted array insertion sort:");
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();

        selectionSort(array); ;
        System.out.println("Sorted array selectionSort sort:");
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();
        List<Integer>last=Arrays.stream(array)
                .sorted()
                .filter(a -> a > 30)
                .boxed()
                .distinct()
                .sorted((a, b) -> Integer.compare(b, a))  //if(a,b) it be sorted
                .limit(2)
                .collect(Collectors.toList());
        System.out.println(last);

    }


}
