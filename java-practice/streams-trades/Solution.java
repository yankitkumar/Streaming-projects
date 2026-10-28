/*
STREAMS ON OBJECTS: 4 small cases on a list of trades.

Run:  javac Solution.java; if ($?) { java Solution }
(No -ea needed. The runner prints PASS or FAIL for each check.)

A Trade has: id, symbol (like "AAPL"), quantity, side.
Side is BUY or SELL.

Same idea as the number drills, but each item is now an object,
so you reach into it:  t -> t.quantity,  t -> t.side == Side.BUY

Drill 1) countBuys          -> how many trades are BUY?
Drill 2) totalBuyQuantity   -> add up the quantity of the BUY trades.
Drill 3) symbolsTraded      -> every symbol that was traded, each one ONCE,
                               sorted A to Z.   NEW: .distinct()  .sorted()
Drill 4) largestSellQuantity -> ALREADY WRITTEN, but it has a bug. Find and fix it.
                               It should return the biggest SELL quantity,
                               or 0 if there are no SELL trades.

Vocabulary:
  .stream()  .filter(t -> cond)  .map(t -> t.symbol)  .mapToInt(t -> t.quantity)
  .count()   .sum()   .max()   .distinct()   .sorted()
  .orElse(0)   .collect(Collectors.toList())
*/

import java.util.*;
import java.util.stream.*;

enum Side { BUY, SELL }

class Trade {
    public int id;
    public String symbol;
    public int quantity;
    public Side side;

    public Trade(int id, String symbol, int quantity, Side side) {
        this.id = id;
        this.symbol = symbol;
        this.quantity = quantity;
        this.side = side;
    }
}

public class Solution {

    // Drill 1
    static long countBuys(List<Trade> trades) {
        return 0; // TODO: replace with a stream
    }

    // Drill 2
    static int totalBuyQuantity(List<Trade> trades) {
        return 0; // TODO: replace with a stream
    }

    // Drill 3
    static List<String> symbolsTraded(List<Trade> trades) {
        return new ArrayList<>(); // TODO: replace with a stream
    }

    // Drill 4: has a bug
    static int largestSellQuantity(List<Trade> trades) {
        return trades.stream()
                .filter(t -> t.side == Side.SELL)
                .mapToInt(t -> t.quantity)
                .findFirst()
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
        List<Trade> trades = Arrays.asList(
                new Trade(1, "AAPL", 10, Side.BUY),
                new Trade(2, "MSFT",  5, Side.SELL),
                new Trade(3, "AAPL", 20, Side.SELL),
                new Trade(4, "GOOG",  8, Side.BUY),
                new Trade(5, "MSFT", 15, Side.BUY),
                new Trade(6, "AAPL",  7, Side.SELL)
        );

        check("1 countBuys", 3L, countBuys(trades));
        check("2 totalBuyQuantity", 33, totalBuyQuantity(trades));
        check("3 symbolsTraded", Arrays.asList("AAPL", "GOOG", "MSFT"), symbolsTraded(trades));
        check("4 largestSellQuantity", 20, largestSellQuantity(trades));

        System.out.println();
        System.out.println(passed + " / " + total + " checks passing");
    }
}
