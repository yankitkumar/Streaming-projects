/*
STREAMS: averages, sorting, anyMatch. 4 small cases on clinic visits.

Run:  javac Solution.java; if ($?) { java Solution }
(No -ea needed. The runner prints PASS or FAIL for each check.)

A Visit has: id, doctor, type, minutes, status.
VisitType is CONSULT, FOLLOWUP or EMERGENCY.  VisitStatus is COMPLETED or CANCELLED.

New vocabulary in this one:
  .average()                         on an IntStream; gives an OptionalDouble,
                                     so finish with .orElse(0.0)
  Collectors.averagingInt(v -> n)    use in groupingBy's 2nd slot: average per pile (Double)
  .sorted(Comparator.comparingInt(v -> v.minutes))
                                     sort objects by one of their int fields, smallest first
  .anyMatch(v -> cond)               true if AT LEAST ONE item matches
  .allMatch(v -> cond)               true only if EVERY item matches

Drill 1) averageCompletedMinutes -> average minutes of the COMPLETED visits
Drill 2) averageMinutesByType    -> type -> average minutes, COMPLETED visits only
                                    (this is the clinic interview Part 2, as a stream)
Drill 3) idsByMinutes            -> the visit ids, sorted by minutes, shortest first
Drill 4) hasCancelled            -> ALREADY WRITTEN, but it has a bug. Find and fix it.
                                    It should be true if ANY visit was cancelled.
*/

import java.util.*;
import java.util.stream.*;

enum VisitType { CONSULT, FOLLOWUP, EMERGENCY }
enum VisitStatus { COMPLETED, CANCELLED }

class Visit {
    public int id;
    public String doctor;
    public VisitType type;
    public int minutes;
    public VisitStatus status;

    public Visit(int id, String doctor, VisitType type, int minutes, VisitStatus status) {
        this.id = id;
        this.doctor = doctor;
        this.type = type;
        this.minutes = minutes;
        this.status = status;
    }
}

public class Solution {

    // Drill 1
    static double averageCompletedMinutes(List<Visit> visits) {
        return 0.0; // TODO: replace with a stream
    }

    // Drill 2
    static Map<VisitType, Double> averageMinutesByType(List<Visit> visits) {
        return new HashMap<>(); // TODO: replace with a stream
    }

    // Drill 3
    static List<Integer> idsByMinutes(List<Visit> visits) {
        return new ArrayList<>(); // TODO: replace with a stream
    }

    // Drill 4: has a bug
    static boolean hasCancelled(List<Visit> visits) {
        return visits.stream()
                .allMatch(v -> v.status == VisitStatus.CANCELLED);
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
        List<Visit> visits = Arrays.asList(
                new Visit(1, "smith", VisitType.CONSULT,    30, VisitStatus.COMPLETED),
                new Visit(2, "smith", VisitType.CONSULT,    41, VisitStatus.COMPLETED),
                new Visit(3, "smith", VisitType.FOLLOWUP,   20, VisitStatus.COMPLETED),
                new Visit(4, "jones", VisitType.EMERGENCY,  40, VisitStatus.COMPLETED),
                new Visit(5, "jones", VisitType.CONSULT,   100, VisitStatus.CANCELLED),
                new Visit(6, "lee",   VisitType.FOLLOWUP,   29, VisitStatus.COMPLETED)
        );

        check("1 averageCompletedMinutes", 32.0, averageCompletedMinutes(visits));
        check("2 averageMinutesByType",
                Map.of(VisitType.CONSULT, 35.5, VisitType.FOLLOWUP, 24.5, VisitType.EMERGENCY, 40.0),
                averageMinutesByType(visits));
        check("3 idsByMinutes", Arrays.asList(3, 6, 1, 4, 2, 5), idsByMinutes(visits));
        check("4 hasCancelled", true, hasCancelled(visits));

        System.out.println();
        System.out.println(passed + " / " + total + " checks passing");
    }
}
