package com.Network.Network.DS.Array;

public class Maxmin {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 10, 19, 1};

        // Create an instance of Maxmin
        Maxmin maxmin = new Maxmin();

        // Call the max and min methods
        int maxValue = maxmin.max(nums);
        int minValue = maxmin.min(nums);

        // Print the results
        System.out.println("Maximum: " + maxValue);
        System.out.println("Minimum: " + minValue);
    }

    public int max(int[] nums) {
        int max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > max) {
                max = nums[i];
            }
        }
        return max;
    }

    public int min(int[] nums) {
        int min = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < min) {
                min = nums[i];
            }
        }
        return min;
    }

}
