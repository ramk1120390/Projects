package com.Network.Network.DS.Array;


public class ReduceStepcount {
    public int numberOfSteps(int num) {
        int count = 0;
        while (num != 0) {
            if (num % 2 == 0) {
                num /= 2;
            } else {
                num -= 1;
            }
            count++;
        }
        return count;
    }

    public static void main(String[] args) {
        ReduceStepcount solution = new ReduceStepcount();
        int num = 4;
        int steps = solution.numberOfSteps(num);
        System.out.println("Number of steps: " + steps);
    }
}


