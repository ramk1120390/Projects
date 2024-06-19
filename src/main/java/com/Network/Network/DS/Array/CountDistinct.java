package com.Network.Network.DS.Array;

import java.util.HashMap;

public class CountDistinct {
    public static void main(String[] args) {
        int[] nums = {10, 30, 40, 20, 10, 20, 50, 10};
        HashMap<Integer,Integer> responce=new HashMap<>();
        for (int num : nums) {
            if (responce.containsKey(num)) {
                responce.put(num, responce.get(num) + 1);
            } else {
                responce.put(num, 1);
            }
        }
        long count = responce.entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .count();
        System.out.println(count);
        System.out.println(responce.size());
    }
}
