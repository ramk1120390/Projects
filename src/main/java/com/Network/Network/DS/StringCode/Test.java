package com.Network.Network.DS.StringCode;

public class Test {
    public static void main(String[] args) {
        String s1 = "sabbutsad";
        String s2 = "sad";
        int index = -1;
        if (s1.contains(s2)) {
            index = s1.indexOf(s2);
        }
        System.out.println(index);
    }
}
