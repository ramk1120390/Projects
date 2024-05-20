package com.Network.Network.DS.Array;

public class RunningSum {
    public static void main(String[] args) {
        int[] array = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        int sum=0;
        int []sumarry=new int[array.length];
        for (int j=0;j< array.length;j++)
        {
            sum+=array[j];
            sumarry[j]=sum;
        }
        for (int i=0;i<sumarry.length;i++)
        {
            System.out.println(sumarry[i]);
        }
    }
}
