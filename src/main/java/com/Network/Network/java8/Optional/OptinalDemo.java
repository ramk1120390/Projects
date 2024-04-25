package com.Network.Network.java8.Optional;

import java.util.Optional;

public class OptinalDemo {
    public static void main(String[] args) {
        String email = "ramk64910@gmail.com";
        String email1 = null;
        //Empty Optional declare
        Optional<String> empty = Optional.empty();
        System.out.println(empty);
        //Optinal of it will before null check if  Value is null throw exception
        Optional<String> nullcheck = Optional.of(email);
        System.out.println(nullcheck.get());
        //Optinal of Nullable it will check before value is  null  it will  return empty
        Optional<String> nullcheckempty = Optional.ofNullable(email1);
        System.out.println(nullcheckempty);
        String defaultoptinal = nullcheckempty.orElse("Default@gmail.com");
        System.out.println(defaultoptinal);
        String defaultoptinal1 = nullcheckempty.orElseGet(() -> "Default@gmail.com");
        System.out.println(defaultoptinal1);
        //  String defaultoptinal2=nullcheckempty.orElseThrow(()->new RuntimeException("Given Email is null"));
        // System.out.println(defaultoptinal1);
        Optional<String> Gender = Optional.of("Male");
        Gender.ifPresent((s) -> System.out.println("value is presnt" + Gender.get()));
        empty.ifPresentOrElse(
                value -> System.out.println("Value is present: " + value), // Action if value is present
                () -> System.out.println("Value is empty") // Action if value is empty
        );
        String result = "response ";
        Optional<String> optionalString = Optional.of(result);
        // Using ifPresent() and orElse()
        optionalString.filter(res -> res.contains("res"))
                .map(String::trim)
                .ifPresentOrElse(
                        value -> System.out.println("Value is present: " + value), // Action if value is present
                        () -> System.out.println("Value does not contain 'res'") // Action if value does not contain 'res'
                );
    }
}
