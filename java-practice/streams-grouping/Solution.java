/*
STREAMS + groupingBy: 4 small cases on a list of expenses.

Run:  javac Solution.java; if ($?) { java Solution }
(No -ea needed. The runner prints PASS or FAIL for each check.)

An Expense has: id, person, category ("food", "rent", "travel"), amount.

groupingBy builds a Map. It has two slots:
    .collect(Collectors.groupingBy( KEY , WHAT_TO_DO_WITH_EACH_GROUP ))
      KEY                     which bucket each item goes in, e.g. e -> e.person
      WHAT_TO_DO_WITH_EACH_GROUP
        Collectors.counting()            how many items in the bucket (Long)
        Collectors.summingInt(e -> n)    add up a number from each item (Integer)

Drill 1) countByCategory    -> category -> how many expenses
Drill 2) totalByCategory    -> category -> total amount
Drill 3) foodTotalByPerson  -> person -> total of their FOOD expenses only
                               (people with no food expenses must not appear)
Drill 4) totalByPerson      -> ALREADY WRITTEN, but it has a bug. Find and fix it.
                               It should be: person -> total of ALL their expenses.
*/

import java.util.*;
import java.util.stream.*;

class Expense {
    public int id;
    public String person;
    public String category;
    public int amount;

    public Expense(int id, String person, String category, int amount) {
        this.id = id;
        this.person = person;
        this.category = category;
        this.amount = amount;
    }
}

public class Solution {

    // Drill 1
    static Map<String, Long> countByCategory(List<Expense> expenses) {
        return new HashMap<>(); // TODO: replace with a stream
    }

    // Drill 2
    static Map<String, Integer> totalByCategory(List<Expense> expenses) {
        return new HashMap<>(); // TODO: replace with a stream
    }

    // Drill 3
    static Map<String, Integer> foodTotalByPerson(List<Expense> expenses) {
        return new HashMap<>(); // TODO: replace with a stream
    }

    // Drill 4: has a bug
    static Map<String, Integer> totalByPerson(List<Expense> expenses) {
        return expenses.stream()
                .collect(Collectors.groupingBy(e -> e.category,
                        Collectors.summingInt(e -> e.amount)));
    }

    // ---------------- test runner (no need to change below) ----------------
    static int passed = 0, total = 0;

    static Object show(Object o) {
        return (o instanceof Map) ? new TreeMap<>((Map<?, ?>) o) : o;   // sorted, easier to read
    }

    static void check(String name, Object expected, Object actual) {
        total++;
        if (Objects.equals(expected, actual)) {
            passed++;
            System.out.println("PASS  " + name);
        } else {
            System.out.println("FAIL  " + name + "   expected " + show(expected) + "   got " + show(actual));
        }
    }

    public static void main(String[] args) {
        List<Expense> expenses = Arrays.asList(
                new Expense(1, "alice", "food",    20),
                new Expense(2, "bob",   "rent",   500),
                new Expense(3, "alice", "travel", 100),
                new Expense(4, "bob",   "food",    30),
                new Expense(5, "alice", "food",    15),
                new Expense(6, "carol", "travel", 200)
        );

        check("1 countByCategory", Map.of("food", 3L, "rent", 1L, "travel", 2L), countByCategory(expenses));
        check("2 totalByCategory", Map.of("food", 65, "rent", 500, "travel", 300), totalByCategory(expenses));
        check("3 foodTotalByPerson", Map.of("alice", 35, "bob", 30), foodTotalByPerson(expenses));
        check("4 totalByPerson", Map.of("alice", 135, "bob", 530, "carol", 200), totalByPerson(expenses));

        System.out.println();
        System.out.println(passed + " / " + total + " checks passing");
    }
}
