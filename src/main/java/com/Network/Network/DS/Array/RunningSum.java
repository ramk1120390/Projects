package com.Network.Network.DS.Array;

public class RunningSum {
    public static void main(String[] args) {
        int[] array = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        int sum = 0;
        int[] sumArray = new int[array.length];

        for (int j = 0; j < array.length; j++) {
            sum += array[j];
            sumArray[j] = sum;
        }

        for (int i = 0; i < sumArray.length; i++) {
            System.out.println(sumArray[i]);
        }
    }
}
