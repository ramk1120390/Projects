package com.Network.Network.DS.Array;

import java.util.stream.IntStream;

public class BestTimeToBuySellStock {

    public static void main(String[] args) {
        int[] prices = {7, 1, 5, 3, 6, 4}; // Example prices array

        int maxProfit = findMaxProfit(prices);
        System.out.println(prices.length);
        System.out.println("Maximum profit: " + maxProfit); // Output: 5
    }

    public static int findMaxProfit(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0; // If there are fewer than 2 prices, profit is 0
        }
        // Using IntStream.range to iterate over indices
        return IntStream.range(1, prices.length) // Stream of indices [1, n-1]
                .map(i -> prices[i] - prices[i - 1]) // Stream of differences [p1-p0, p2-p1, ..., pn-p(n-1)]
                .filter(profit -> profit > 0) // Filter positive profits (where selling price > buying price)
                .sum(); // Sum of all positive profits



    }
}
