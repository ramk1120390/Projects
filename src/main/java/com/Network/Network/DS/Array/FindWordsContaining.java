package com.Network.Network.DS.Array;

import java.util.ArrayList;
import java.util.List;

public class FindWordsContaining {

    public static void main(String[] args) {
        FindWordsContaining solution = new FindWordsContaining();
        String[] words = {"leet", "code"};
        char x = 'e';
        List<Integer> result = solution.findWordsContaining(words, x);
        System.out.println("Indices of words containing '" + x + "': " + result);
    }

    public List<Integer> findWordsContaining(String[] words, char x) {
        List<Integer> output = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            if (words[i].indexOf(x) != -1) {
                output.add(i);
            }
        }
        return output;
    }
}
