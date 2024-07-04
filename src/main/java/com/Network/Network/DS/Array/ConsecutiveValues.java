package com.Network.Network.DS.Array;

import java.util.Arrays;

public class ConsecutiveValues {
    public static void main(String[] args) {
        int[] coins = {1, 1, 1, 4};
        Arrays.sort(coins);
        int max = 0;
        for (int coin : coins) {
            if (coin <= max + 1) {
                max = max + coin;
            } else {
                break;
            }
        }
        System.out.println(max+1);

    }
    //Iteration:
    //Use 1: maxReach becomes 0 + 1 = 1
    //Use 1: maxReach becomes 1 + 1 = 2
    //Use 1: maxReach becomes 2 + 1 = 3
    //Use 4: maxReach becomes 3 + 4 = 7
    //Maximum consecutive values: 0, 1, 2, 3, 4, 5, 6, 7 → Output: 8
}
