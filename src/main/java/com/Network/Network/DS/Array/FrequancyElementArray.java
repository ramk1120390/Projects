package com.Network.Network.DS.Array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FrequancyElementArray {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 10, 19, 1};
        HashMap<Integer,Integer>responce=new HashMap<>();
        for (int num : nums) {
            if (responce.containsKey(num)) {
                responce.put(num, responce.get(num) + 1);
            } else {
                responce.put(num, 1);
            }
        }
        System.out.println(responce);
        Map<Integer, Long> frequencyMap = Arrays.stream(nums)
                .boxed()  // Convert int to Integer
                .collect(
                        HashMap::new,
                        (map, num) -> {
                            map.put(num, map.getOrDefault(num, 0L) + 1);
                        },
                        HashMap::putAll
                );
        System.out.println(frequencyMap);
    }
}

