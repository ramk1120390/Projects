package com.Network.Network.java8.Default;

interface Card {
    String brand(String name);

    default String Cardcount() {
        return "1000";
    }

    default Integer Price() {
        return 100;
    }

    static String getDetails() {
        return "cardcount";
    }
}

public class Default {
    public static void main(String[] args) {
        Card card = new Card() {
            @Override
            public String brand(String name) {
                return "Welcome " + name;
            }
        };

        // Accessing Price and getDetails through the implemented instance
        Integer price = card.Price();
        String count=card.Cardcount();
        String details = Card.getDetails();


        System.out.println(card.brand("Bmw"));
        System.out.println("Price: " + price);
        System.out.println("Details: " + details);
        System.out.println("Count: " + count);
    }
}
