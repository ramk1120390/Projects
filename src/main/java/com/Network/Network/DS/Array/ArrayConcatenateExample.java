package com.Network.Network.DS.Array;

import java.util.ArrayList;
import java.util.List;

public class ArrayConcatenateExample {

    public static void main(String[] args) {
        int[] nums = {1, 2, 1};
        int[] ans = concatenateArray(nums);
        for (int num : ans) {
            System.out.print(num + " ");
        }
        // Output: [1, 2, 1, 1, 2, 1]
    }

    public static int[] concatenateArray(int[] nums) {
        int[] ans = new int[nums.length * 2];
        for (int i = 0; i < nums.length; i++) {
            ans[i] = nums[i];
            ans[i + nums.length] = nums[i];
        }
        return ans;
    }
}
