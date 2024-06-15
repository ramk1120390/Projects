package com.Network.Network.DS.Array;

public class LeftRightSumDiff {
    public static void main(String[] args) {
        int[] nums = {10, 4, 8, 3};
        int[] leftSum = new int[nums.length];
        int[] rightSum = new int[nums.length];
        int[] finalAbs = new int[nums.length];

        int sum = 0;
        // Calculate cumulative sum from left to right
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            leftSum[i] = sum;
        }

        sum = 0;
        // Calculate cumulative sum from right to left
        for (int i = nums.length - 1; i >= 0; i--) {
            sum += nums[i];
            rightSum[i] = sum;
        }

        // Calculate absolute differences between leftSum and rightSum
        for (int i = 0; i < nums.length; i++) {
            finalAbs[i] = Math.abs(leftSum[i] - rightSum[i]);
        }

        // Print the final result
        for (int num : finalAbs) {
            System.out.print(num + " ");
        }
    }
}
