package com.Network.Network.DS.Array;

public class IncrementOperations {
    public static void main(String[] args) {
        int operations = 0;
        int[] nums = {1, 5, 2, 4, 1};
        for (int i = 1; i < nums.length; i++)
            if (nums[i] <= nums[i - 1]) {
                // Calculate the difference needed to make the current element greater
                int diff = nums[i - 1] - nums[i] + 1;
                // Increment the current element
                nums[i] += diff;
                // Add the number of operations to the counter
                operations += diff;
            }
        System.out.println(operations);

    }
}
/*Initial array: [1, 1, 1]

First iteration (i = 1):

nums[1] <= nums[0] (1 <= 1)
diff = nums[0] - nums[1] + 1 = 1 - 1 + 1 = 1
nums[1] = nums[1] + diff = 1 + 1 = 2
operations = operations + diff = 0 + 1 = 1
Array becomes: [1, 2, 1]
Second iteration (i = 2):

nums[2] <= nums[1] (1 <= 2)
diff = nums[1] - nums[2] + 1 = 2 - 1 + 1 = 2
nums[2] = nums[2] + diff = 1 + 2 = 3
operations = operations + diff = 1 + 2 = 3
Array becomes: [1, 2, 3]
Final array: [1, 2, 3]

Total operations: 3/*
