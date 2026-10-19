/*
SUPER BASIC EXERCISE: methods on an int array. No classes, no enums.

Run with assertions ON:   javac Solution.java; if ($?) { java -ea Solution }

Part 1) testSum fails. There is a bug in sum(). Find it and fix it.

Part 2) Implement the stub   static double average(int[] nums)
        Return the average of the numbers.
        Empty array -> return 0.0.
        Test: testAverage.

Tests stop at the first failing assert. Fix one thing, re-run.
*/

public class Solution {

    static int sum(int[] nums) {
        int total = 0;
        for (int i = 0; i < nums.length; i++) {
            total = nums[i];
        }
        return total;
    }

    // Part 2 - TODO: replace this stub with a real implementation
    static double average(int[] nums) {
        return 0.0;
    }


    public static void main(String[] args) {
        testSum();
        testAverage();
        System.out.println("All tests pass!");
    }

    static void testSum() {
        System.out.println("Running testSum");
        assert sum(new int[]{}) == 0 : "empty array should sum to 0";
        assert sum(new int[]{5}) == 5 : "sum of {5} should be 5";
        int s = sum(new int[]{10, 20, 30});
        assert s == 60 : "sum of {10,20,30} should be 60, was " + s;
    }

    static void testAverage() {
        System.out.println("Running testAverage");
        double a = average(new int[]{1, 2});
        assert Math.abs(a - 1.5) < 1e-4 : "average of {1,2} should be 1.5, was " + a;
        assert Math.abs(average(new int[]{10, 20, 30}) - 20.0) < 1e-4 : "average of {10,20,30} should be 20.0";
        assert average(new int[]{}) == 0.0 : "empty array should give 0.0";
    }
}
