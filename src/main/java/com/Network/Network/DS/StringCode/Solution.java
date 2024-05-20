package com.Network.Network.DS.StringCode;

import java.util.ArrayList;
public class Solution {
    public static void main(String[] args) {
        String s = "123A156B7";
        ArrayList<Integer> numbers = new ArrayList<>();
        StringBuilder currentNumber = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                currentNumber.append(c);
            } else {
                if (currentNumber.length() > 0) {
                    numbers.add(Integer.parseInt(currentNumber.toString()));
                    currentNumber.setLength(0);
                }
            }
        }
        if (currentNumber.length() > 0) {
            numbers.add(Integer.parseInt(currentNumber.toString()));
        }
        System.out.println(numbers);
    }
}
