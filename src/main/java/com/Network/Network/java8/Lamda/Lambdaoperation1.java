package com.Network.Network.java8.Lamda;


interface Addable {
    int add(int a, int b);
}

class AD1 implements Addable {
    @Override
    public int add(int a, int b) {
        return a + b;

    }
}

class AD2 implements Addable {
    @Override
    public int add(int a, int b) {
        return a * b;

    }
}


public class Lambdaoperation1 {
    public static void main(String[] args) {
///lamda code
        Addable ad1 = (a, b) -> (a + b);
        System.out.println(ad1.add(10, 20));
        Addable ad2 = (a, b) -> {
            return a * b;
        };
        System.out.println(ad2.add(100, 8));

        System.out.println("without lambda");
        //Without lambda
        Addable Ad1 = new AD1();
        System.out.println(Ad1.add(10, 20));
        Addable Ad2 = new AD2();
        System.out.println(Ad2.add(10, 20));
    }
}
