package com.Network.Network.DS.StringCode;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.HashMap;

public class CountOccurrence {
    public static void main(String[] args) {
        String s = "123A156B7";
        HashMap<Character, Integer> count = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            count.put(c, count.getOrDefault(c, 0) + 1);
            //System.out.println(count);
        }

        System.out.println(count);
    }
}

