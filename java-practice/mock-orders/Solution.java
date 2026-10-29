/*
We are building the back-end for a trading desk order system.
The system tracks traders and the orders they place.

Definitions:
* A "trader" has: traderId, name.
* An "order" has: orderId, traderId, symbol, quantity, status, orderType.
* OrderStatus is one of: FILLED, OPEN, CANCELLED, REJECTED.
* OrderType is one of: MARKET, LIMIT, STOP.
* "OrderManager" manages traders, orders, and provides statistics.

To begin with, we present you with two tasks:
1-1) Read through and understand the code below. Feel free to run it.
1-2) The test for OrderManager is not passing due to a bug in the code.
     Make the necessary changes to OrderManager to fix the bug.
*/

// part2
/*
We are extending the platform to report on filled volume per symbol.

Recall that each Order carries a symbol (for example "AAPL") and a quantity.

Add a new function to the OrderManager class:

2) The getFilledQuantityBySymbol function should take a traderId and return
   a dictionary mapping each symbol to the total quantity that trader has
   FILLED for that symbol. Only orders with a FILLED status should be counted;
   ignore orders with any other status. Only symbols the trader has at least
   one filled order for should appear in the dictionary. If the trader has no
   filled orders at all, or the trader does not exist, return an empty
   dictionary.

To assist you in testing this new function, we have provided the
testGetFilledQuantityBySymbol test.
*/

import java.util.*;

enum OrderStatus {
    FILLED, OPEN, CANCELLED, REJECTED
}

enum OrderType {
    MARKET, LIMIT, STOP
}

class Trader {
    public int traderId;
    public String name;

    public Trader(int traderId, String name) {
        this.traderId = traderId;
        this.name = name;
    }
}

class Order {
    public int orderId;
    public int traderId;
    public String symbol;
    public int quantity;
    public OrderStatus status;
    public OrderType orderType;

    public Order(int orderId, int traderId, String symbol, int quantity,
                 OrderStatus status, OrderType orderType) {
        this.orderId = orderId;
        this.traderId = traderId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.status = status;
        this.orderType = orderType;
    }
}

class OrderStats {
    public int totalOrders;
    public int filledOrders;
    public double cancelRate;

    public OrderStats(int totalOrders, int filledOrders, double cancelRate) {
        this.totalOrders = totalOrders;
        this.filledOrders = filledOrders;
        this.cancelRate = cancelRate;
    }
}

class OrderManager {
    public Map<Integer, Trader> traders;
    public List<Order> orders;

    public OrderManager() {
        traders = new HashMap<>();
        orders = new ArrayList<>();
    }

    public void addTrader(Trader trader) {
        traders.put(trader.traderId, trader);
    }

    public void addOrder(Order order) {
        if (traders.containsKey(order.traderId)) {
            return;
        }
        orders.add(order);
    }

    public OrderStats getOrderStatistics() {
        int total = orders.size();

        int filled = 0;
        for (Order o : orders) {
            if (o.status == OrderStatus.FILLED) {
                filled++;
            }
        }

        int cancelled = 0;
        for (Order o : orders) {
            if (o.status == OrderStatus.CANCELLED) {
                cancelled++;
            }
        }

        double cancelRate;
        if (total > 0) {
            cancelRate = (double) cancelled / total;
        } else {
            cancelRate = 0.0;
        }

        return new OrderStats(total, filled, cancelRate);
    }
}

public class Solution {
    public static void main(String[] args) {
        testGetOrderStatistics();
        testGetFilledQuantityBySymbol();
        System.out.println("All tests pass!");
    }

    public static void testGetOrderStatistics() {
        System.out.println("Running testGetOrderStatistics");
        OrderManager om = new OrderManager();
        om.addTrader(new Trader(10, "alice"));

        om.addOrder(new Order(1, 10, "AAPL", 100, OrderStatus.FILLED,    OrderType.MARKET));
        om.addOrder(new Order(2, 10, "MSFT",  50, OrderStatus.FILLED,    OrderType.LIMIT));
        om.addOrder(new Order(3, 10, "AAPL",  30, OrderStatus.CANCELLED, OrderType.LIMIT));
        om.addOrder(new Order(4, 10, "GOOG",  20, OrderStatus.OPEN,      OrderType.LIMIT));
        om.addOrder(new Order(5, 10, "MSFT",  10, OrderStatus.REJECTED,  OrderType.MARKET));
        // unknown trader -> ignored
        om.addOrder(new Order(6, 99, "AAPL", 500, OrderStatus.FILLED,    OrderType.MARKET));

        OrderStats stats = om.getOrderStatistics();
        assert stats.totalOrders == 5 :
            "totalOrders should be 5, was " + stats.totalOrders;
        assert stats.filledOrders == 2 :
            "filledOrders should be 2, was " + stats.filledOrders;
        assert Math.abs(stats.cancelRate - 0.2) < 1e-4 :
            "cancelRate should be 0.2, was " + stats.cancelRate;
    }

    static void testGetFilledQuantityBySymbol() {
        System.out.println("Running testGetFilledQuantityBySymbol");
        OrderManager om = new OrderManager();
        om.addTrader(new Trader(1, "alice"));
        om.addTrader(new Trader(2, "bob"));
        om.addTrader(new Trader(3, "carol"));
        om.addTrader(new Trader(4, "dave"));

        om.addOrder(new Order(1, 1, "AAPL", 100, OrderStatus.FILLED,    OrderType.MARKET));
        om.addOrder(new Order(2, 1, "AAPL",  50, OrderStatus.FILLED,    OrderType.LIMIT));
        om.addOrder(new Order(3, 1, "MSFT",  20, OrderStatus.FILLED,    OrderType.STOP));
        // not FILLED -> excluded
        om.addOrder(new Order(4, 1, "AAPL", 999, OrderStatus.CANCELLED, OrderType.LIMIT));
        om.addOrder(new Order(5, 1, "GOOG", 500, OrderStatus.OPEN,      OrderType.LIMIT));
        om.addOrder(new Order(6, 2, "GOOG",  70, OrderStatus.FILLED,    OrderType.MARKET));
        om.addOrder(new Order(7, 2, "MSFT",  40, OrderStatus.REJECTED,  OrderType.MARKET));
        om.addOrder(new Order(8, 4, "MSFT",  30, OrderStatus.FILLED,    OrderType.LIMIT));
        om.addOrder(new Order(9, 4, "MSFT",   5, OrderStatus.FILLED,    OrderType.MARKET));

        Map<String, Integer> q1 = om.getFilledQuantityBySymbol(1);
        assert Objects.equals(150, q1.get("AAPL")) : "Expected 150 for AAPL, got " + q1.get("AAPL");  // 100 + 50
        assert Objects.equals(20, q1.get("MSFT")) : "Expected 20 for MSFT, got " + q1.get("MSFT");
        assert !q1.containsKey("GOOG") : "GOOG should not be in q1";

        Map<String, Integer> q2 = om.getFilledQuantityBySymbol(2);
        assert Objects.equals(70, q2.get("GOOG")) : "Expected 70 for GOOG, got " + q2.get("GOOG");
        assert q2.size() == 1 : "Only GOOG expected in q2, got " + q2;

        Map<String, Integer> q4 = om.getFilledQuantityBySymbol(4);
        assert Objects.equals(35, q4.get("MSFT")) : "Expected 35 for MSFT, got " + q4.get("MSFT");  // 30 + 5
        assert q4.size() == 1 : "Only MSFT expected in q4, got " + q4;

        // trader with no orders
        assert om.getFilledQuantityBySymbol(3).isEmpty() : "Expected empty map for trader with no orders";

        // unknown traderId -> empty map
        assert om.getFilledQuantityBySymbol(999).isEmpty() : "Expected empty map for unknown traderId";
    }
}
