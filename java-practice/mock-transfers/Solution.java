/*
We are building the back-end for a cash transfer platform.
The system tracks bank accounts and the transfers made from them.

Definitions:
* A "bank account" has: accountId, name.
* A "transfer" has: transferId, accountId, counterpartyId,
  amount, status, transferType.
* TransferStatus is one of: SETTLED, PENDING, FAILED, RETURNED.
* TransferType is one of: WIRE, ACH, INTERNAL.
* "TransferManager" manages accounts, transfers, and provides statistics.

To begin with, we present you with two tasks:
1-1) Read through and understand the code below. Feel free to run it.
1-2) The test for TransferManager is not passing due to a bug in the code.
     Make the necessary changes to TransferManager to fix the bug.
*/

// part2
/*
We are extending the platform to report on transfer amounts by type.

Recall that each Transfer carries a transferType, one of
WIRE, ACH, INTERNAL.

Add a new function to the TransferManager class:

2) The getAverageSettledAmountByType function should take an accountId and
   return a dictionary mapping each transfer type (WIRE, ACH, INTERNAL) to
   that account's average transfer amount for that type. Only transfers with
   a SETTLED status should be counted; ignore transfers with any other status.
   Only transfer types the account has at least one settled transfer for
   should appear in the dictionary. If the account has no settled transfers
   at all, or the account does not exist, return an empty dictionary.

To assist you in testing this new function, we have provided the
testGetAverageSettledAmountByType test.
*/

import java.util.*;

enum TransferStatus {
    SETTLED, PENDING, FAILED, RETURNED
}

enum TransferType {
    WIRE, ACH, INTERNAL
}

class BankAccount {
    public int accountId;
    public String name;

    public BankAccount(int accountId, String name) {
        this.accountId = accountId;
        this.name = name;
    }
}

class Transfer {
    public int transferId;
    public int accountId;
    public int counterpartyId;
    public int amount;
    public TransferStatus status;
    public TransferType transferType;

    public Transfer(int transferId, int accountId, int counterpartyId,
                    int amount, TransferStatus status, TransferType transferType) {
        this.transferId = transferId;
        this.accountId = accountId;
        this.counterpartyId = counterpartyId;
        this.amount = amount;
        this.status = status;
        this.transferType = transferType;
    }
}

class TransferStats {
    public int totalTransfers;
    public int settledTransfers;
    public double failureRate;

    public TransferStats(int totalTransfers, int settledTransfers, double failureRate) {
        this.totalTransfers = totalTransfers;
        this.settledTransfers = settledTransfers;
        this.failureRate = failureRate;
    }
}

class TransferManager {
    public Map<Integer, BankAccount> accounts;
    public List<Transfer> transfers;

    public TransferManager() {
        accounts = new HashMap<>();
        transfers = new ArrayList<>();
    }

    public void addAccount(BankAccount account) {
        accounts.put(account.accountId, account);
    }

    public void addTransfer(Transfer transfer) {
        if (!accounts.containsKey(transfer.accountId)) {
            return;
        }
        transfers.add(transfer);
    }

    public TransferStats getTransferStatistics() {
        int total = transfers.size();

        int settled = 0;
        for (Transfer t : transfers) {
            if (t.status == TransferStatus.SETTLED) {
                settled++;
            }
        }

        int failed = 0;
        for (Transfer t : transfers) {
            if (t.status == TransferStatus.FAILED) {
                failed++;
            }
        }

        double failureRate;
        if (total > 0) {
            failureRate = (double) (failed / total);
        } else {
            failureRate = 0.0;
        }

        return new TransferStats(total, settled, failureRate);
    }
}

public class Solution {
    public static void main(String[] args) {
        testGetTransferStatistics();
        testGetAverageSettledAmountByType();
        System.out.println("All tests pass!");
    }

    public static void testGetTransferStatistics() {
        System.out.println("Running testGetTransferStatistics");
        TransferManager tm = new TransferManager();
        tm.addAccount(new BankAccount(10, "acme_corp"));

        tm.addTransfer(new Transfer(1, 10, 500, 1000, TransferStatus.SETTLED,  TransferType.WIRE));
        tm.addTransfer(new Transfer(2, 10, 501,  500, TransferStatus.SETTLED,  TransferType.ACH));
        tm.addTransfer(new Transfer(3, 10, 502,  200, TransferStatus.FAILED,   TransferType.WIRE));
        tm.addTransfer(new Transfer(4, 10, 503,  300, TransferStatus.PENDING,  TransferType.INTERNAL));
        tm.addTransfer(new Transfer(5, 10, 504,  250, TransferStatus.RETURNED, TransferType.ACH));
        // unknown account -> ignored
        tm.addTransfer(new Transfer(6, 99, 505,  700, TransferStatus.FAILED,   TransferType.WIRE));

        TransferStats stats = tm.getTransferStatistics();
        assert stats.totalTransfers == 5 :
            "totalTransfers should be 5, was " + stats.totalTransfers;
        assert stats.settledTransfers == 2 :
            "settledTransfers should be 2, was " + stats.settledTransfers;
        assert Math.abs(stats.failureRate - 0.2) < 1e-4 :
            "failureRate should be 0.2, was " + stats.failureRate;
    }

    static void testGetAverageSettledAmountByType() {
        System.out.println("Running testGetAverageSettledAmountByType");
        TransferManager tm = new TransferManager();
        tm.addAccount(new BankAccount(1, "acme_corp"));
        tm.addAccount(new BankAccount(2, "globex"));
        tm.addAccount(new BankAccount(3, "initech"));
        tm.addAccount(new BankAccount(4, "umbrella"));
        tm.addAccount(new BankAccount(5, "hooli"));

        tm.addTransfer(new Transfer(1, 1, 100, 1000, TransferStatus.SETTLED,  TransferType.WIRE));
        tm.addTransfer(new Transfer(2, 1, 101, 1001, TransferStatus.SETTLED,  TransferType.WIRE));
        tm.addTransfer(new Transfer(3, 1, 102,  200, TransferStatus.SETTLED,  TransferType.ACH));
        // not SETTLED -> excluded from averages
        tm.addTransfer(new Transfer(4, 1, 103, 5000, TransferStatus.FAILED,   TransferType.WIRE));
        tm.addTransfer(new Transfer(5, 1, 104, 9000, TransferStatus.PENDING,  TransferType.WIRE));
        tm.addTransfer(new Transfer(6, 2, 105,  400, TransferStatus.SETTLED,  TransferType.INTERNAL));
        tm.addTransfer(new Transfer(7, 4, 106,  250, TransferStatus.SETTLED,  TransferType.WIRE));
        tm.addTransfer(new Transfer(8, 5, 107,  150, TransferStatus.SETTLED,  TransferType.ACH));
        tm.addTransfer(new Transfer(9, 5, 108,  999, TransferStatus.RETURNED, TransferType.ACH));

        Map<TransferType, Double> avg1 = tm.getAverageSettledAmountByType(1);
        assert Math.abs(1000.5 - avg1.get(TransferType.WIRE)) < 1e-4 : "Expected 1000.5 for WIRE";  // (1000+1001)/2
        assert Math.abs(200.0 - avg1.get(TransferType.ACH)) < 1e-4 : "Expected 200.0 for ACH";
        assert !avg1.containsKey(TransferType.INTERNAL) : "INTERNAL should not be in avg1";

        Map<TransferType, Double> avg2 = tm.getAverageSettledAmountByType(2);
        assert Math.abs(400.0 - avg2.get(TransferType.INTERNAL)) < 1e-4 : "Expected 400.0 for INTERNAL";
        assert !avg2.containsKey(TransferType.WIRE) : "WIRE should not be in avg2";
        assert !avg2.containsKey(TransferType.ACH) : "ACH should not be in avg2";

        Map<TransferType, Double> avg4 = tm.getAverageSettledAmountByType(4);
        assert Math.abs(250.0 - avg4.get(TransferType.WIRE)) < 1e-4 : "Expected 250.0 for WIRE";
        assert avg4.size() == 1 : "Only WIRE expected in avg4";

        Map<TransferType, Double> avg5 = tm.getAverageSettledAmountByType(5);
        assert Math.abs(150.0 - avg5.get(TransferType.ACH)) < 1e-4 : "Expected 150.0 for ACH (RETURNED excluded)";
        assert avg5.size() == 1 : "Only ACH expected in avg5";

        // account with no transfers
        assert tm.getAverageSettledAmountByType(3).isEmpty() : "Expected empty map for account with no transfers";

        // unknown accountId -> empty map
        assert tm.getAverageSettledAmountByType(999).isEmpty() : "Expected empty map for unknown accountId";
    }
}
