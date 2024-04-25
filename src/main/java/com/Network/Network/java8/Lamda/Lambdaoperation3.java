package com.Network.Network.java8.Lamda;

interface Message {
    String hi(String msg);
}

class Inbox implements Message {
    @Override
    public String hi(String msg) {
        return "Welcome" + msg;
    }
}

public class Lambdaoperation3 {
    public static void main(String[] args) {
        Message msg = (ms) -> {
            return "Welcome" + ms;
        };
        System.out.println(msg.hi("Ram"));
        Message msg1 = new Inbox();
        System.out.println(msg1.hi("kumar"));
        Message person = (message)-> {
            String str1 = "I would like to say, ";
            String str2 = str1 + message;
            return str2;
        };
        System.out.println(person.hi("time is precious."));
    }
}

