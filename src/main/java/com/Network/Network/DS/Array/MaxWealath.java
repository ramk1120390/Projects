package com.Network.Network.DS.Array;

class MaxWealath {
    public int maximumWealth(int[][] accounts) {
        int maxWealth = 0;
        for (int[] customer : accounts) {
            int currentWealth = 0;
            for (int bank : customer) {
                currentWealth += bank;
            }
            if (currentWealth > maxWealth) {
                maxWealth = currentWealth;
            }
        }
        return maxWealth;
    }

    public static void main(String[] args) {
        MaxWealath solution = new MaxWealath();
        int[][] accounts1 = { {1, 2, 3}, {3, 2, 1} }; //6 6
        int result1 = solution.maximumWealth(accounts1);
        System.out.println("Maximum wealth: " + result1);
    }
}
