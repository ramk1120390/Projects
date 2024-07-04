package com.Network.Network.DS.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveDuplicate {
    public static void main(String[] args) {
        int[] array = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        HashMap<Integer, Integer> counts = new HashMap<>();
        ArrayList<Integer> unique = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            counts.put(array[i], counts.getOrDefault(array[i], 0) + 1);
        }

        for (HashMap.Entry<Integer, Integer> entry : counts.entrySet()) {
                unique.add(entry.getKey()); // Add the unique element to the list
        }
        System.out.println(unique);
        List<Integer> count =Arrays.stream(array).distinct().boxed().collect(Collectors.toList());
        System.out.println(count);
    }
}
