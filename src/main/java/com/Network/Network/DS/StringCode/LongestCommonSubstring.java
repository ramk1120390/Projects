package com.Network.Network.DS.StringCode;

import java.util.Arrays;

public class LongestCommonSubstring {
    public String longestCommonPrefix(String[] arr) {
        int size = arr.length;
        if (size == 0) {
            return "";
        } else if (size == 1) {
            return arr[0];
        }
        Arrays.sort(arr);
        /* find the minimum length from first and last string */
        int end = Math.min(arr[0].length(), arr[size - 1].length());
        int i = 0;
        while (i < end && arr[0].charAt(i) == arr[size - 1].charAt(i))
            i++;
        String pre = arr[0].substring(0, i);
        return pre;
    }

    public static void main(String[] args) {
        String[] input = {"geeksforgeeks", "geeks", "gks", "geeszer"};
        LongestCommonSubstring responce = new LongestCommonSubstring();
        String ans = responce.longestCommonPrefix(input);
        System.out.println(ans);
    }

}
