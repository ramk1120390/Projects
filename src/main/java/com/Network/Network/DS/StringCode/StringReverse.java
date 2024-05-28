package com.Network.Network.DS.StringCode;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StringReverse {
    public static void main(String[] args) {
        String s="Ramkumar";
        String rev= IntStream.rangeClosed(1,s.length())
                .mapToObj(i->s.charAt(s.length()-i)).map(String::valueOf).collect(Collectors.joining());
        System.out.println(rev);
    }


}
