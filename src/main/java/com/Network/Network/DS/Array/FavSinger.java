package com.Network.Network.DS.Array;

import java.util.*;

public class FavSinger {
    public static void main(String args[]) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (int i = 0; i < n; i++) {
            int num = scanner.nextInt();
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        int maxCount = 0;
        int favcount = 0;
        for (int count : frequencyMap.values()) {
            maxCount = Math.max(maxCount, count);
        }
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() == maxCount) {
                favcount++;
            }
        }
       System.out.println(favcount);
    }
}
