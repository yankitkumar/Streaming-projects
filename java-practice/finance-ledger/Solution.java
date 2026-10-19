/*
We are building the back-end for a payments ledger.
The system tracks accounts and transactions.

Definitions:
* An "account" has: accountId, owner.
* A "transaction" has: txId, accountId, amountCents, status, txType.
* TxStatus is one of: POSTED, PENDING, FAILED, REVERSED.
* TxType is one of: DEPOSIT, WITHDRAWAL, FEE.
* "LedgerManager" manages accounts, transactions, and provides statistics.

Part 1)
  Read and understand the code. Run it WITH assertions: java -ea Solution
  testGetLedgerStatistics is failing because of a bug in LedgerManager.
  Find it and fix it.

Part 2)
  Add to LedgerManager:
    Map<TxType, Double> getAverageAmountByType(int accountId)
  Return, for that account, the average amountCents per TxType.
  - Only POSTED transactions count; ignore every other status.
  - Only types with at least one POSTED transaction appear in the map.
  - No POSTED transactions, or unknown accountId -> empty map.
  Test: testGetAverageAmountByType (currently won't compile until you add it).
*/

import java.util.*;

enum TxStatus { POSTED, PENDING, FAILED, REVERSED }
enum TxType { DEPOSIT, WITHDRAWAL, FEE }

class Account {
    public int accountId;
    public String owner;
    public Account(int accountId, String owner) { this.accountId = accountId; this.owner = owner; }
}

class Transaction {
    public int txId;
    public int accountId;
    public int amountCents;
    public TxStatus status;
    public TxType txType;
    public Transaction(int txId, int accountId, int amountCents, TxStatus status, TxType txType) {
        this.txId = txId; this.accountId = accountId; this.amountCents = amountCents;
        this.status = status; this.txType = txType;
    }
}

class LedgerStats {
    public int totalTransactions;
    public int postedTransactions;
    public double failureRate;
    public LedgerStats(int total, int posted, double failureRate) {
        this.totalTransactions = total; this.postedTransactions = posted; this.failureRate = failureRate;
    }
}

class LedgerManager {
    public Map<Integer, Account> accounts = new HashMap<>();
    public List<Transaction> transactions = new ArrayList<>();

    public void addAccount(Account a) { accounts.put(a.accountId, a); }

    public void addTransaction(Transaction t) {
        if (!accounts.containsKey(t.accountId)) return;
        transactions.add(t);
    }

    public LedgerStats getLedgerStatistics() {
        int total = transactions.size();

        int posted = 0;
        for (Transaction t : transactions) {
            if (t.status == TxStatus.POSTED || t.status == TxStatus.PENDING) {
                posted++;
            }
        }

        int failed = 0;
        for (Transaction t : transactions) {
            if (t.status == TxStatus.FAILED) failed++;
        }

        double failureRate = 0.0;
        if (total > 0) {
            failureRate = failed / total;
        }
        return new LedgerStats(total, posted, failureRate);
    }

    // TODO Part 2: getAverageAmountByType(int accountId)
}

public class Solution {
    public static void main(String[] args) {
        testGetLedgerStatistics();
        testGetAverageAmountByType();
        System.out.println("All tests pass!");
    }

    static void testGetLedgerStatistics() {
        System.out.println("Running testGetLedgerStatistics");
        LedgerManager lm = new LedgerManager();
        lm.addAccount(new Account(10, "alice"));
        lm.addTransaction(new Transaction(1, 10, 5000, TxStatus.POSTED,   TxType.DEPOSIT));
        lm.addTransaction(new Transaction(2, 10, 2000, TxStatus.POSTED,   TxType.WITHDRAWAL));
        lm.addTransaction(new Transaction(3, 10, 1500, TxStatus.FAILED,   TxType.WITHDRAWAL));
        lm.addTransaction(new Transaction(4, 10, 300,  TxStatus.PENDING,  TxType.FEE));
        lm.addTransaction(new Transaction(5, 10, 800,  TxStatus.REVERSED, TxType.DEPOSIT));
        lm.addTransaction(new Transaction(6, 99, 100,  TxStatus.POSTED,   TxType.DEPOSIT)); // unknown account

        LedgerStats s = lm.getLedgerStatistics();
        assert s.totalTransactions == 5 : "total should be 5, was " + s.totalTransactions;
        assert s.postedTransactions == 2 : "posted should be 2, was " + s.postedTransactions;
        assert Math.abs(s.failureRate - 0.2) < 1e-4 : "failureRate should be 0.2, was " + s.failureRate;
    }

    static void testGetAverageAmountByType() {
        System.out.println("Running testGetAverageAmountByType");
        LedgerManager lm = new LedgerManager();
        lm.addAccount(new Account(1, "alice"));
        lm.addAccount(new Account(2, "bob"));
        lm.addAccount(new Account(3, "carol"));

        lm.addTransaction(new Transaction(1, 1, 1000, TxStatus.POSTED,   TxType.DEPOSIT));
        lm.addTransaction(new Transaction(2, 1, 1001, TxStatus.POSTED,   TxType.DEPOSIT));
        lm.addTransaction(new Transaction(3, 1, 200,  TxStatus.POSTED,   TxType.FEE));
        lm.addTransaction(new Transaction(4, 1, 9999, TxStatus.PENDING,  TxType.DEPOSIT));   // ignored
        lm.addTransaction(new Transaction(5, 1, 9999, TxStatus.FAILED,   TxType.WITHDRAWAL)); // ignored
        lm.addTransaction(new Transaction(6, 2, 400,  TxStatus.POSTED,   TxType.WITHDRAWAL));

        Map<TxType, Double> a1 = lm.getAverageAmountByType(1);
        assert Math.abs(a1.get(TxType.DEPOSIT) - 1000.5) < 1e-4 : "Expected 1000.5 for DEPOSIT";
        assert Math.abs(a1.get(TxType.FEE) - 200.0) < 1e-4 : "Expected 200.0 for FEE";
        assert !a1.containsKey(TxType.WITHDRAWAL) : "WITHDRAWAL must not appear for account 1";

        Map<TxType, Double> a2 = lm.getAverageAmountByType(2);
        assert Math.abs(a2.get(TxType.WITHDRAWAL) - 400.0) < 1e-4 : "Expected 400.0";
        assert a2.size() == 1 : "Only WITHDRAWAL expected for account 2";

        assert lm.getAverageAmountByType(3).isEmpty() : "Expected empty for account with no tx";
        assert lm.getAverageAmountByType(999).isEmpty() : "Expected empty for unknown account";
    }
}
