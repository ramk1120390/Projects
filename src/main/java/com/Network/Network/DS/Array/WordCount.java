package com.Network.Network.DS.Array;

public class WordCount {
    public static void main(String[] args) {
        String[] words = {"alice and bob love leetcode", "i think so too", "this is great thanks very much"};
        int max = 0;
        for (int i = 0; i < words.length; i++) {
            String[] parts = words[i].split("\\s+"); // Splitting each word into parts
            int wordCount = parts.length; // Counting the number of parts
            if (wordCount > max) {
                max = wordCount;
            }
        }
        System.out.println("Maximum word count: " + max);
    }
}
