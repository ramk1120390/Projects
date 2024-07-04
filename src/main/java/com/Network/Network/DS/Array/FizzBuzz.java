package com.Network.Network.DS.Array;

import java.util.ArrayList;
import java.util.List;

public class FizzBuzz {
    public static void main(String[] args) {
        List<String> responce = new ArrayList<>();
        int n = 15;
        for (int i = 1; i <= n; i++) {
            if (i % 3 == 0 && i % 5 == 0) {
                responce.add("FizzBuzz");
            } else if (i % 3 == 0) {
                responce.add("Fizz");
            } else if (i % 5 == 0) {
                responce.add("Buzz");
            } else
                responce.add(String.valueOf(i));
        }
        System.out.println(responce);
    }


}
