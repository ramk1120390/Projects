package com.Network.Network.java8.Stream;

import com.Network.Network.DevicemetamodelPojo.Customer;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Employee {
    public static List<Customers> getAll() {
        return Stream.of(
                new Customers(101, "1.3", "Ramk64910@gmail.com", Arrays.asList("6383814434", "6383814435"), 10000),
                new Customers(102, "1.4", "another@example.com", Arrays.asList("1234567890", "9876543210"), 10000),
                new Customers(103, "1.4", "another@example.com", Arrays.asList("1234567890", "9876543210"), 15000),
                new Customers(104, "1.4", "another@example.com", Arrays.asList("1234567890", "9876543210"), 25000)
        ).collect(Collectors.toList());
    }

    public static List<Customers> evaluate(String input) {
        return (input.equals("NONIT")) ?
                getAll().stream().filter(cus -> cus.getEmail().contains("another")).collect(Collectors.toList()) :
                getAll().stream().filter(customers -> customers.getEmail().contains("Ramk")).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> isEven = num -> num % 2 == 0;
        //Method1:boolean allMatch
        boolean allEven = numbers.stream().allMatch(isEven);
        if (!allEven) {
            System.out.print("Not all numbers are even. Odd numbers: ");
            numbers.stream()
                    .filter(num -> !isEven.test(num))
                    .forEach(num -> System.out.print(num + " "));
            System.out.println();
        } else {
            System.out.println("All numbers are even.");
        }
        //Method2:boolean any match
        boolean anyEven = numbers.stream().anyMatch(isEven);
        if (anyEven) {
            System.out.print("found and. even numbers: ");
            numbers.stream()
                    .filter(num -> isEven.test(num))
                    .forEach(num -> System.out.print(num + " "));
            System.out.println();
        } else {
            System.out.println("No number is even.");
        }
        //Method3:Stream builder
        Stream.Builder<String> builder = Stream.builder();
        builder.add("Hello");
        builder.add("World");
        Stream<String> stream = builder.build();
        stream.forEach(System.out::println);
        //method4:Collecters
        Stream<String> stream1 = Stream.of("apple", "banana", "cherry");
        List<String> collectedList = stream1.collect(Collectors.toList());
        System.out.println(collectedList);
        //method4:Concat
        Stream<Integer> stream2 = Stream.of(1, 2, 3);
        Stream<Integer> stream3 = Stream.of(4, 5, 6);
        Stream<Integer> concatenatedStream = Stream.concat(stream2, stream3);
        //methods 4:Stream count we can perform only operation stream if print not able count
        //long count = concatenatedStream.count();
        //System.out.println("Number of elements: " + count);

        //concatenatedStream.forEach(System.out::println);
        List<Integer> collectedList1 = concatenatedStream.collect(Collectors.toList());
        System.out.println(collectedList1);
        //methods 5:Stream Distinct
        Stream<Integer> streamWithDuplicates = Stream.of(1, 2, 3, 2, 4, 3, 5);
        Stream<Integer> streamWithDuplicates1 = Stream.of(1, 2, 3, 2, 4, 3, 5);

        // Get a stream with distinct elements
        Stream<Integer> distinctStream = streamWithDuplicates.distinct();
        List<Integer> collectedList2 = distinctStream.collect(Collectors.toList());
        System.out.println(collectedList2);
        List<Integer> repeatedValues = streamWithDuplicates1
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Print the repeated values
        System.out.println(repeatedValues);
        //Declare empty Stream
        Stream<String> emptyStream = Stream.empty();
        //find any random
        Stream<String> stream4 = Stream.of("apple", "banana", "cherry");

        // Find any element from the stream
        //Optional<String> result = stream4.findAny();
        // System.out.println(result.get());
        Optional<String> result1 = stream4.findFirst();
        System.out.println(result1.get());

        ///map vs FlatMap
        List<Customers> customers1 = getAll();
        List<String> emails = customers1.stream().map(Customers::getEmail).collect(Collectors.toList());
        System.out.println(emails);
        List<List<String>> phonenumbers = customers1.stream().map(customers -> customers.getPhonenumber()).collect(Collectors.toList());
        System.out.println(phonenumbers);
        List<String> phonenumbers1 = customers1.stream().flatMap(customers -> customers.getPhonenumber().stream()).collect(Collectors.toList());
        System.out.println(phonenumbers1);
        Map<String, List<String>> namePhoneMap = customers1.stream()
                .flatMap(customer -> customer.getPhonenumber().stream()
                        .map(phoneNumber1 -> Map.entry(customer.getName(), phoneNumber1)))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));
        System.out.println(namePhoneMap);
        List<Double> response = customers1.stream()
                .flatMapToDouble(customer -> DoubleStream.of(Double.parseDouble(customer.getName())))
                .boxed() // Convert double values to Double objects
                .collect(Collectors.toList());
        System.out.println(response);

        /*List<Integer> response1 = getAll().stream()
                .flatMapToInt(customer -> IntStream.of(Integer.parseInt(customer.getName())))
                .boxed() // Convert int values to Integer objects
                .collect(Collectors.toList());

         */
        List<Integer> numbers1 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        int sumOfDoubledEvenNumbers = numbers1.stream()      // Create a stream from the list
                .filter(n -> n % 2 == 0)                   // Filter out the even numbers
                .mapToInt(n -> n * 2)                      // Double each even number
                .sum();                                    // Find the sum of all doubled even numbers

        System.out.println("Sum of doubled even numbers: " + sumOfDoubledEvenNumbers);
        List<Integer> numbers2 = Arrays.asList(10, 5, 20, 15, 30);
        Optional<Integer> maxNumber = numbers2.stream()
                .distinct()
                .max(Integer::compareTo);
        System.out.println(maxNumber.get());

        List<Integer> numbers3 = Arrays.asList(10, 5, 20, 15, 30);

        // Find the second largest number using Stream API
        Optional<Integer> secondLargest = numbers3.stream()
                .sorted((a, b) -> b.compareTo(a)) // Sort in descending order
                .skip(1) // Skip the first element (largest number)
                .findFirst(); // Get the second element
        System.out.println(secondLargest.get());

        List<Integer> numbers4 = Arrays.asList(10, 5, 20, 15, 30);

        // Find the second largest number after removing duplicates
        Optional<Integer> secondLargest1 = numbers4.stream()
                .distinct() // Remove duplicates
                .sorted(Comparator.reverseOrder()) // Sort in descending order
                .skip(1) // Skip the first element (largest number)
                .findFirst(); // Get the second element
        System.out.println(secondLargest1.get());
        List<Integer> numbers5 = Arrays.asList(1, 2, 3, 4, 5);

        // Using reduce to calculate the sum of all elements
        Optional<Integer> sumOptional = numbers5.stream()
                .reduce((a, b) -> a + b);
        System.out.println("Sum of all elements: " + sumOptional.get());
        List<Integer> numbers6 = Arrays.asList(1, 2, 3, 4, 5);

        // Using reduce to calculate the sum of all elements with an initial value
        Integer sum = numbers6.stream()
                .reduce(0, (a, b) -> a + b);

        // Print the sum
        System.out.println("Sum of all elements: " + sum);
        List<String> list = new ArrayList<>();
        list.add("Ramkumar");
        list.add("Kumar");
        list.add("Raja");
        list.add("Sample");
        list.stream().filter((t) -> t.contains("K")).forEach(t -> System.out.println(t));
        HashMap<String, String> maps = new HashMap<>();
        maps.put("Loopback", "interface");
        maps.put("Tunnel", "interface");
        maps.put("1/2/3port", "Ports");
        maps.forEach((key, value) -> System.out.println(key + " " + value));
        maps.entrySet().stream().filter(k -> k.getKey().contains("L")).forEach(s -> System.out.println(s));
        System.out.println(evaluate("IT"));

        ///java 8 new feautures:
        //1:Stream of Nullable
        List<String> names = Arrays.asList("Ramkumar", "Java", "Phyton", null, "Raja");
        List<String> nullablenames = names.stream().filter(name -> name != null).collect(Collectors.toList());
        System.out.println(nullablenames);
        //another method
        List<String> nullableNames1 = names.stream().filter(Objects::nonNull).collect(Collectors.toList());
        List<String> nullableNames2 = names.stream().flatMap(Stream::ofNullable).collect(Collectors.toList());
        System.out.println(nullableNames2);
        System.out.println(nullableNames1);

        //2:Stream Iterate
        List<Integer> collect = Stream.iterate(0, n -> n + 5).filter(n -> n % 4 == 0).limit(5).collect(Collectors.toList());
        System.out.println(collect);

        //3:Collectors.CollectingAndThen
        Long Customerbudget = getAll().stream().mapToDouble(Customers::getIncome).boxed().
                collect(Collectors.collectingAndThen(Collectors.averagingDouble(Double::doubleValue), Math::round));
        Long customerBudget1 = getAll()
                .stream()
                .mapToDouble(Customers::getIncome)
                .boxed()
                .collect(Collectors.collectingAndThen(Collectors.summarizingDouble(Double::doubleValue), summary ->
                        Math.round(summary.getSum())));
        System.out.println(customerBudget1);
        //4:Stream takeWhile and Drop while
        List<Integer> integerList = Arrays.asList(1, 2, 4, 5, 7, 8, 10, 19, 18);
        List<Integer> takeWhile = integerList.stream().takeWhile(num -> num < 5).collect(Collectors.toList());
        System.out.println(takeWhile);
        List<Integer> DropWhile = integerList.stream().dropWhile(num -> num < 5).collect(Collectors.toList());
        System.out.println(DropWhile);
        List<Integer> combine = integerList.stream().dropWhile(num -> num < 5).takeWhile(num -> num <= 7).collect(Collectors.toList());
        System.out.println(combine);
        //5:Collectors teeing
        Map<String, String> minmaxMap = integerList.stream()
                .collect(Collectors.teeing(
                        Collectors.maxBy(Integer::compareTo),
                        Collectors.minBy(Integer::compareTo),
                        (maxOpt, minOpt) -> {
                            String max = maxOpt.map(String::valueOf).orElse("No max value");
                            String min = minOpt.map(String::valueOf).orElse("No min value");
                            return Map.of("max", max, "min", min);
                        }
                ));

        System.out.println(minmaxMap);
        //6:Stream Concat
        Stream<Integer> input1 = Stream.of(1, 2, 4, 5, 6);
        Stream<Integer> input2 = Stream.of(7, 9, 10, 11, 12);
        Stream<Integer> reponce = Stream.concat(input1, input2);
        //Integer sum1=reponce.mapToInt(Integer::intValue).sum();
        List<Integer> responce1 = reponce.collect(Collectors.toList());
        // System.out.println(sum1);
        System.out.println(responce1);
        List<Integer> integerList1 = Arrays.asList(1, 2, 4, 5, 7, 8, 10, 19, 18);
        //7:Partion by
        Map<Boolean, List<Integer>> map = integerList1.stream().collect(Collectors.partitioningBy(num -> num % 2 == 0));
        System.out.println(map);
        System.out.println("odd number " + map.get(Boolean.FALSE));
        System.out.println("Even number " + map.get(Boolean.TRUE));
        //8:IntStream ranges
        List<Integer> instream1 = IntStream.range(1, 20).boxed().collect(Collectors.toList());
        System.out.println(instream1);
        List<Integer> instream2 = IntStream.rangeClosed(1, 20).boxed().collect(Collectors.toList());
        System.out.println(instream2);


        //Sorted Salary based asc

        List<Customers> salasc = getAll().stream().sorted((c1, c2) -> Integer.compare(c1.getIncome(), c2.getIncome()))
                .collect(Collectors.toList());


    }


}




