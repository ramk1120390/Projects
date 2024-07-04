package com.Network.Network.DS.Array;

import java.util.*;
import java.util.stream.Collectors;

public class interchangeableRectangles {
    public long interchangeableRectangles(int[][] rectangles) {
        Map<Double, Long> ratioCountMap = Arrays.stream(rectangles)
                .map(rectangle -> (double) rectangle[0] / rectangle[1])
                .collect(Collectors.groupingBy(r -> r, Collectors.counting()));
        long pairs = 0;
        for (long count : ratioCountMap.values()) {
            if (count > 1) {
                pairs += (count * (count - 1)) / 2; // Combination formula: nC2 = n * (n - 1) / 2
            }
        }
        return pairs;
    }

    public static void main(String[] args) {
        interchangeableRectangles solution = new interchangeableRectangles();
        int[][] rectangles1 = {{4, 8}, {3, 6}, {10, 20}, {15, 30}, {20, 10}};
        int[][] rectangles2 = {{4, 4}, {7, 7}};

        System.out.println(solution.interchangeableRectangles(rectangles1)); // Output: 6
        System.out.println(solution.interchangeableRectangles(rectangles2)); // Output: 0
    }
}
