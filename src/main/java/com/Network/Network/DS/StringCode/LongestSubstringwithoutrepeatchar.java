package com.Network.Network.DS.StringCode;

import java.util.LinkedHashMap;
import java.util.Map;

public class LongestSubstringwithoutrepeatchar {
    public static void longsub(String s) {
        String longsubstr = "";
        int longsubstrlen = 0;
        Map<Character, Integer> response = new LinkedHashMap<>();
        char[] arr = s.toCharArray();

        for (int i = 0; i < arr.length; i++) {
            char ch = arr[i];
            if (!response.containsKey(ch)) {
                response.put(ch, i);
            } else {
                // If the character is repeated, update the start index
                i = response.get(ch);
                response.clear();
            }

            // Check if the current length of response map is greater than the previous longest substring length
            if (response.size() > longsubstrlen) {
                longsubstrlen = response.size();
                longsubstr = response.keySet().toString();
            }
        }

        System.out.println("Longest Substring without repeating characters: " + longsubstr);
        System.out.println("Length of the longest substring: " + longsubstrlen);
    }

    public static void main(String[] args) {
        String test = "abcabcbb";
        longsub(test);

        test = "bbbbb";
        longsub(test);

        test = "pwwkew";
        longsub(test);
    }
}
