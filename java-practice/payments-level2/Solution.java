/*
LEVEL 2 EXERCISE: payments. One small class, a list, a status filter, a Map.

Run with assertions ON:   javac Solution.java; if ($?) { java -ea Solution }

A Payment has: id, customer (String), amount (int, whole dollars), status.
Status is one of: PAID, PENDING, FAILED.
PaymentManager keeps a list of payments.

Part 1) testPaidTotal fails. There is a bug in getPaidTotal(). Find it and fix it.

Part 2) Implement getTotalByCustomer():
        Return a Map<String, Integer>: customer name -> total amount of that
        customer's PAID payments.
        - Only PAID payments count.
        - A customer with no PAID payments must NOT appear in the map.
        - No payments at all -> empty map.

Tests stop at the first failing assert. Fix one thing, re-run.
*/

import java.util.*;

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
        int total = 0;
        for (Payment p : payments) {
            if (p.status == Status.PAID || p.status == Status.PENDING) {
                total += p.amount;
            }
        }
        return total;
    }

    // Part 2 - TODO: replace this stub with a real implementation
    public Map<String, Integer> getTotalByCustomer() {
        return new HashMap<>();
    }
}

public class Solution {
    public static void main(String[] args) {
        testPaidTotal();
        testTotalByCustomer();
        System.out.println("All tests pass!");
    }

    static PaymentManager sample() {
        PaymentManager pm = new PaymentManager();
        pm.add(new Payment(1, "alice", 100, Status.PAID));
        pm.add(new Payment(2, "bob",    50, Status.PENDING));
        pm.add(new Payment(3, "alice",  70, Status.PAID));
        pm.add(new Payment(4, "bob",    30, Status.FAILED));
        pm.add(new Payment(5, "bob",    20, Status.PAID));
        pm.add(new Payment(6, "carol",  90, Status.FAILED));
        return pm;
    }

    static void testPaidTotal() {
        System.out.println("Running testPaidTotal");
        assert new PaymentManager().getPaidTotal() == 0 : "no payments should total 0";
        int t = sample().getPaidTotal();
        assert t == 190 : "paid total should be 190, was " + t;
    }

    static void testTotalByCustomer() {
        System.out.println("Running testTotalByCustomer");
        Map<String, Integer> m = sample().getTotalByCustomer();
        assert m.size() == 2 : "expected 2 customers (alice, bob), got " + m;
        assert m.get("alice") != null && m.get("alice") == 170 : "alice should be 170, got " + m.get("alice");
        assert m.get("bob") != null && m.get("bob") == 20 : "bob should be 20, got " + m.get("bob");
        assert !m.containsKey("carol") : "carol has no PAID payments, must not appear";
        assert new PaymentManager().getTotalByCustomer().isEmpty() : "no payments -> empty map";
    }
}
