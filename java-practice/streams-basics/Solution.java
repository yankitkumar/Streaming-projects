/*
STREAMS BASICS: 4 small cases on a list of numbers.

Run:  javac Solution.java; if ($?) { java Solution }
(No -ea needed. The runner prints PASS or FAIL for each check.)

Before writing each one, say on paper what comes out of every step.

Drill 1) countGreaterThan7  -> how many numbers are greater than 7?
Drill 2) doubled            -> a new list with every number doubled.
Drill 3) sumOfEvens         -> the sum of the even numbers only.
Drill 4) largestOdd         -> ALREADY WRITTEN, but it has a bug. Find and fix it.
                               It should return the largest odd number,
                               or 0 if there are no odd numbers.

Vocabulary:
  .stream()  .filter(n -> cond)  .map(n -> thing)  .mapToInt(n -> n)
  .count()   .sum()   .max().orElse(0)   .collect(Collectors.toList())
  even: n % 2 == 0      odd: n % 2 != 0
*/

import java.util.*;
import java.util.stream.*;

public class Solution {

    // Drill 1
    static long countGreaterThan7(List<Integer> nums) {
        return 0; // TODO: replace with a stream
    }

    // Drill 2
    static List<Integer> doubled(List<Integer> nums) {
        return new ArrayList<>(); // TODO: replace with a stream
    }

    // Drill 3
    static int sumOfEvens(List<Integer> nums) {
        return 0; // TODO: replace with a stream
    }

    // Drill 4: has a bug
    static int largestOdd(List<Integer> nums) {
        return nums.stream()
                .filter(n -> n % 2 == 0)
                .mapToInt(n -> n)
                .max()
                .orElse(0);
    }

    // ---------------- test runner (no need to change below) ----------------
    static int passed = 0, total = 0;

    static void check(String name, Object expected, Object actual) {
        total++;
        if (Objects.equals(expected, actual)) {
            passed++;
            System.out.println("PASS  " + name);
        } else {
            System.out.println("FAIL  " + name + "   expected " + expected + "   got " + actual);
        }
    }

    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(5, 12, 8, 20, 3);

        check("1 countGreaterThan7", 3L, countGreaterThan7(nums));
        check("2 doubled", Arrays.asList(10, 24, 16, 40, 6), doubled(nums));
        check("3 sumOfEvens", 40, sumOfEvens(nums));
        check("4 largestOdd", 5, largestOdd(nums));
        check("4 largestOdd (no odds)", 0, largestOdd(Arrays.asList(2, 4)));

        System.out.println();
        System.out.println(passed + " / " + total + " checks passing");
    }
}
