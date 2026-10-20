/*
STREAMS PRACTICE, level 1. Same payments idea as before, now with streams.

Run with assertions ON:   javac Solution.java; if ($?) { java -ea Solution }

Stream cheat sheet (no answers, just the vocabulary):
  list.stream()                  start a stream from a list
  .filter(p -> condition)        keep only items where the condition is true
  .map(p -> p.something)         turn each item into something else
  .mapToInt(p -> p.number)       turn each item into an int (so .sum() works)
  .distinct()                    remove duplicates
  .sorted()                      sort (A to Z for Strings)
  .sum()                         add up an IntStream
  .collect(Collectors.toList())  finish: gather the results into a List

Part 1) testPaidTotal fails. getPaidTotal() is written with a stream and has
        a bug. Find it and fix it. (Think: what does the filter let through?)

Part 2) Implement getPaidCustomerNames() USING A STREAM:
        Return a List<String> of the customer names that have at least one
        PAID payment. Each name appears once, sorted A to Z.
        No PAID payments -> empty list.

Tests stop at the first failing assert. Fix one thing, re-run.
*/

import java.util.*;
import java.util.stream.*;

enum Status { PAID, PENDING, FAILED }

class Payment {
    public int id;
    public String customer;
    public int amount;
    public Status status;

    public Payment(int id, String customer, int amount, Status status) {
        this.id = id;
        this.customer = customer;
        this.amount = amount;
        this.status = status;
    }
}

class PaymentManager {
    public List<Payment> payments = new ArrayList<>();

    public void add(Payment p) {
        payments.add(p);
    }

    public int getPaidTotal() {
        return payments.stream()
                .filter(p -> p.status != Status.FAILED)
                .mapToInt(p -> p.amount)
                .sum();
    }

    // Part 2 - TODO: replace this stub with a stream
    public List<String> getPaidCustomerNames() {
        return new ArrayList<>();
    }
}

public class Solution {
    public static void main(String[] args) {
        testPaidTotal();
        testPaidCustomerNames();
        System.out.println("All tests pass!");
    }

    static PaymentManager sample() {
        PaymentManager pm = new PaymentManager();
        pm.add(new Payment(1, "zoe",   100, Status.PAID));
        pm.add(new Payment(2, "bob",    50, Status.PENDING));
        pm.add(new Payment(3, "zoe",    70, Status.PAID));
        pm.add(new Payment(4, "bob",    30, Status.FAILED));
        pm.add(new Payment(5, "bob",    20, Status.PAID));
        pm.add(new Payment(6, "carol",  90, Status.FAILED));
        pm.add(new Payment(7, "adam",   10, Status.PAID));
        return pm;
    }

    static void testPaidTotal() {
        System.out.println("Running testPaidTotal");
        assert new PaymentManager().getPaidTotal() == 0 : "no payments should total 0";
        int t = sample().getPaidTotal();
        assert t == 200 : "paid total should be 200, was " + t;
    }

    static void testPaidCustomerNames() {
        System.out.println("Running testPaidCustomerNames");
        List<String> names = sample().getPaidCustomerNames();
        assert names.equals(Arrays.asList("adam", "bob", "zoe"))
                : "expected [adam, bob, zoe] (no duplicates, sorted), got " + names;
        assert new PaymentManager().getPaidCustomerNames().isEmpty() : "no payments -> empty list";
    }
}
