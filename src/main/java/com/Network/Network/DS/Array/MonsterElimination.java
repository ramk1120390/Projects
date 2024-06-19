package com.Network.Network.DS.Array;

import java.util.Arrays;

public class MonsterElimination {

    public static int eliminateMaximum(int[] dist, int[] speed) {
        int n = dist.length;
        double[] timeToReach = new double[n];

        // Calculate the time each monster will take to reach you
        for (int i = 0; i < n; i++) {
            timeToReach[i] = (double) dist[i] / speed[i];
        }

        // Sort the times in ascending order
        Arrays.sort(timeToReach);

        int eliminated = 0;

        // Iterate over each monster
        for (int minute = 0; minute < n; minute++) {

            if (minute < timeToReach[minute]) {
                eliminated++;
            } else {
                // If any monster reaches you, break the loop
                break;
            }
        }

        return eliminated;
    }

    public static void main(String[] args) {
        // Test case 1
        int[] dist1 = {1, 3, 4};
        int[] speed1 = {1, 1, 1};
        System.out.println(eliminateMaximum(dist1, speed1));  // Output: 3

        // Test case 2
        int[] dist2 = {4, 3, 4};
        int[] speed2 = {1, 1, 2};
        System.out.println(eliminateMaximum(dist2, speed2));  // Output: 3
    }
}
