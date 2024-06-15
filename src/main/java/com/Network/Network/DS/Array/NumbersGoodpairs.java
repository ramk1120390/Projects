package com.Network.Network.DS.Array;

import java.util.Arrays;

public class NumbersGoodpairs {
    public static void main(String[] args) {
        int[] array = {1,2,3,1,1,3};
        int count = 0;
        Arrays.sort(array);
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] == array[j]) {
                    count++;
                }
            }
        }
       System.out.println(count);
    }
}
