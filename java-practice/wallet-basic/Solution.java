/*
BASIC EXERCISE: a tiny wallet.

A Wallet holds a list of transactions. Each transaction has an amount (whole
dollars) and a type: DEPOSIT adds money, WITHDRAWAL removes money.

Run with assertions ON:   javac Solution.java; if ($?) { java -ea Solution }

Part 1) testBalance fails. There is a bug in Wallet.getBalance(). Find and fix it.

Part 2) Implement Wallet.getAverageDeposit():
        the average amount of DEPOSIT transactions, as a double.
        No deposits -> 0.0.

Tests stop at the first failing assert. Fix one thing, re-run, repeat.
*/

import java.util.*;

enum TxType { DEPOSIT, WITHDRAWAL }

class Transaction {
    public int amount;
    public TxType type;

    public Transaction(int amount, TxType type) {
        this.amount = amount;
        this.type = type;
    }
}

class Wallet {
    public List<Transaction> transactions = new ArrayList<>();

    public void add(Transaction t) {
        transactions.add(t);
    }

    public int getBalance() {
        int balance = 0;
        for (int i = 0; i < transactions.size() - 1; i++) {
            Transaction t = transactions.get(i);
            if (t.type == TxType.DEPOSIT) {
                balance += t.amount;
            } else {
                balance -= t.amount;
            }
        }
        return balance;
    }

    // Part 2 - TODO
    public double getAverageDeposit() {
        return 0.0;
    }
}

public class Solution {
    public static void main(String[] args) {
        testBalance();
        testAverageDeposit();
        System.out.println("All tests pass!");
    }

    static Wallet sampleWallet() {
        Wallet w = new Wallet();
        w.add(new Transaction(100, TxType.DEPOSIT));
        w.add(new Transaction(51,  TxType.DEPOSIT));
        w.add(new Transaction(30,  TxType.WITHDRAWAL));
        return w;
    }

    static void testBalance() {
        System.out.println("Running testBalance");
        assert new Wallet().getBalance() == 0 : "empty wallet balance should be 0";
        int b = sampleWallet().getBalance();
        assert b == 121 : "balance should be 121, was " + b;
    }

    static void testAverageDeposit() {
        System.out.println("Running testAverageDeposit");
        double avg = sampleWallet().getAverageDeposit();
        assert Math.abs(avg - 75.5) < 1e-4 : "average deposit should be 75.5, was " + avg;
        assert new Wallet().getAverageDeposit() == 0.0 : "no deposits should give 0.0";
        Wallet onlyW = new Wallet();
        onlyW.add(new Transaction(10, TxType.WITHDRAWAL));
        assert onlyW.getAverageDeposit() == 0.0 : "only withdrawals should give 0.0";
    }
}
