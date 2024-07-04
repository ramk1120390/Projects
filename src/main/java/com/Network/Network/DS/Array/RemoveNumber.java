package com.Network.Network.DS.Array;

public class RemoveNumber {
    public int removeElement(int[] nums, int val) {
        int k = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                nums[k++] = nums[i];
            }
        }
        return k;
    }

    public static void main(String[] args) {
        RemoveNumber solution = new RemoveNumber();
        int[] nums1 = {3, 2, 2, 3};
        int val1 = 3;
        int ans1 = solution.removeElement(nums1, val1);
        System.out.println("Length of nums1 after removal: " + ans1);
        System.out.print("Array nums1 after removal: ");
        for (int i = 0; i < ans1; i++) {
            System.out.print(nums1[i] + " ");
        }
        System.out.println();

        int[] nums2 = {0, 1, 2, 2, 3, 0, 4, 2};
        int val2 = 2;
        int ans2 = solution.removeElement(nums2, val2);
        System.out.println("Length of nums2 after removal: " + ans2);
        System.out.print("Array nums2 after removal: ");
        for (int i = 0; i < ans2; i++) {
            System.out.print(nums2[i] + " ");
        }
        System.out.println();
    }
}
