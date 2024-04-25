package com.Network.Network.java8.Methodrefrance;


import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;


interface Message {
    void message(String msg);
}

public class MethodRefranceDemo {

    public void display(String msg) {
        msg = msg.toLowerCase();
        System.out.println(msg);
    }

    public static int addtion(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {

        //method1:Reference the static method
        //with lambda
        Function<Integer, Double> doubleFunction = (input) -> Math.sqrt(input);
        System.out.println(doubleFunction.apply(2));

        //without lambda
        BiFunction<Integer, Integer, Integer> sumfumction = Math::addExact;
        System.out.println(sumfumction.apply(20, 30));

        //calling static method with lambda
        BiFunction<Integer, Integer, Integer> sumfunction = (a, b) -> MethodRefranceDemo.addtion(a, b);
        System.out.println(sumfunction.apply(20, 10));
        //calling static method without lambda
        BiFunction<Integer, Integer, Integer> sumfunction1 = MethodRefranceDemo::addtion;
        System.out.println(sumfunction1.apply(10, 200));

        //method2:Reference the instance  method of object
        //with lambda Function
        MethodRefranceDemo methodRefranceDemo = new MethodRefranceDemo();
        Message print = (a) -> methodRefranceDemo.display(a);
        print.message("Welcome");
        //calling without lambda
        Message print1 = methodRefranceDemo::display;
        print1.message("Welcome");

        //Method 3 reference to and instance method to an arbitrary Method
//without lambda
        Function<String, String> uppercase = (x) -> x.toLowerCase();
        System.out.println(uppercase.apply("india"));
        //with lambda
        BiFunction<String, String, String> uppercase1 = String::concat;
        System.out.println(uppercase1.apply("india", "country").toUpperCase());
        String[] stringArray = {"A", "S", "K", "c", "D"};

        // Using Lambda
        Arrays.sort(stringArray, (s1, s2) -> s1.compareToIgnoreCase(s2));
        System.out.println("Sorted array using lambda:");
        printArray(stringArray);

        // Using Method Reference
        Arrays.sort(stringArray, String::compareToIgnoreCase);
        System.out.println("Sorted array using method reference:");
        printArray(stringArray);

        //Method 4 reference to a Constructor
        List<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Apple");
        fruits.add("Cherry");
        //with lammbda
        Function<List<String>, Set<String>> setFunction = (fruitslist) -> new HashSet<>(fruitslist);
        System.out.println(setFunction.apply(fruits));
        Function<List<String>, Set<String>> setFunction1 = HashSet::new;
        System.out.println(setFunction1.apply(fruits));


    }


    public static void printArray(String[] arr) {
        for (String element : arr) {
            System.out.print(element + " ");
        }
        System.out.println();
    }
}
