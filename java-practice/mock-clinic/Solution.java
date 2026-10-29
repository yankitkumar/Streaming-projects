/*
We are building the back-end for a clinic appointment management system.
The system tracks doctors and appointments.

Definitions:
* A "doctor" has: doctorId, name.
* An "appointment" has: appointmentId, doctorId, patientId,
  durationMinutes, status, appointmentType.
* AppointmentStatus is one of: SCHEDULED, COMPLETED, CANCELLED, NO_SHOW.
* AppointmentType is one of: CONSULTATION, FOLLOWUP, EMERGENCY.
* "ClinicManager" manages doctors, appointments, and provides statistics.

To begin with, we present you with two tasks:
1-1) Read through and understand the code below. Feel free to run it.
1-2) The test for ClinicManager is not passing due to a bug in the code.
     Make the necessary changes to ClinicManager to fix the bug.
*/

// part2
/*
We are extending the platform to report on appointment durations by type.

Recall that each Appointment carries an appointmentType, one of
CONSULTATION, FOLLOWUP, EMERGENCY.

Add a new function to the ClinicManager class:

2) The getAverageAppointmentDurationByType function should return a
   dictionary mapping each appointment type (CONSULTATION, FOLLOWUP,
   EMERGENCY) to the doctor's average appointment duration in minutes for
   that type. Only appointments with a COMPLETED status should be counted;
   ignore appointments with any other status. Only appointment types the
   doctor has at least one completed appointment for should appear in the
   dictionary. If the doctor has no completed appointments at all, return
   an empty dictionary.

To assist you in testing this new function, we have provided the
testGetAverageAppointmentDurationByType test.
*/

import java.util.*;

enum AppointmentStatus {
    SCHEDULED, COMPLETED, CANCELLED, NO_SHOW
}

enum AppointmentType {
    CONSULTATION, FOLLOWUP, EMERGENCY
}

class Doctor {
    public int doctorId;
    public String name;

    public Doctor(int doctorId, String name) {
        this.doctorId = doctorId;
        this.name = name;
    }
}

class Appointment {
    public int appointmentId;
    public int doctorId;
    public int patientId;
    public int durationMinutes;
    public AppointmentStatus status;
    public AppointmentType appointmentType;

    public Appointment(int appointmentId, int doctorId, int patientId,
                       int durationMinutes, AppointmentStatus status,
                       AppointmentType appointmentType) {
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.durationMinutes = durationMinutes;
        this.status = status;
        this.appointmentType = appointmentType;
    }
}

class AppointmentStats {
    public int totalAppointments;
    public int completedAppointments;
    public double noShowRate;

    public AppointmentStats(int totalAppointments, int completedAppointments, double noShowRate) {
        this.totalAppointments = totalAppointments;
        this.completedAppointments = completedAppointments;
        this.noShowRate = noShowRate;
    }
}

class ClinicManager {
    public Map<Integer, Doctor> doctors;
    public List<Appointment> appointments;

    public ClinicManager() {
        doctors = new HashMap<>();
        appointments = new ArrayList<>();
    }

    public void addDoctor(Doctor doctor) {
        doctors.put(doctor.doctorId, doctor);
    }

    public void addAppointment(Appointment appointment) {
        if (!doctors.containsKey(appointment.doctorId)) {
            return;
        }
        appointments.add(appointment);
    }

    public AppointmentStats getAppointmentStatistics() {
        int total = appointments.size();

        int completed = 0;
        for (Appointment a : appointments) {
            if (a.status == AppointmentStatus.COMPLETED || a.status == AppointmentStatus.NO_SHOW) {
                completed++;
            }
        }

        int noShows = 0;
        for (Appointment a : appointments) {
            if (a.status == AppointmentStatus.NO_SHOW) {
                noShows++;
            }
        }

        double noShowRate;
        if (total > 0) {
            noShowRate = (double) noShows / total;
        } else {
            noShowRate = 0.0;
        }

        return new AppointmentStats(total, completed, noShowRate);
    }
}


public class Solution {
    public static void main(String[] args) {
        testGetAppointmentStatistics();
        testGetAverageAppointmentDurationByType();
        System.out.println("All tests pass!");
    }

    public static void testGetAppointmentStatistics() {
        System.out.println("Running testGetAppointmentStatistics");
        ClinicManager cm = new ClinicManager();
        cm.addDoctor(new Doctor(10, "dr_smith"));

        cm.addAppointment(new Appointment(1, 10, 100, 30, AppointmentStatus.COMPLETED, AppointmentType.CONSULTATION));

        cm.addAppointment(new Appointment(2, 10, 101, 45, AppointmentStatus.COMPLETED, AppointmentType.FOLLOWUP));

        cm.addAppointment(new Appointment(3, 10, 102, 30, AppointmentStatus.NO_SHOW, AppointmentType.EMERGENCY));

        cm.addAppointment(new Appointment(4, 10, 103, 60, AppointmentStatus.CANCELLED, AppointmentType.CONSULTATION));

        cm.addAppointment(new Appointment(5, 10, 104, 30, AppointmentStatus.SCHEDULED, AppointmentType.FOLLOWUP));


        AppointmentStats stats = cm.getAppointmentStatistics();
        assert stats.totalAppointments == 5 :
            "totalAppointments should be 5, was " + stats.totalAppointments;

        assert stats.completedAppointments == 2 :
            "completedAppointments should be 2, was " + stats.completedAppointments;

        assert Math.abs(stats.noShowRate - 0.2) < 1e-4 :
            "noShowRate should be 0.2, was " + stats.noShowRate;

      }
      
    static void testGetAverageAppointmentDurationByType() {
        System.out.println("Running testGetAverageAppointmentDurationByType");
        ClinicManager cm = new ClinicManager();
        cm.addDoctor(new Doctor(1, "dr_smith"));
        cm.addDoctor(new Doctor(2, "dr_jones"));
        cm.addDoctor(new Doctor(3, "dr_brown"));
        cm.addDoctor(new Doctor(4, "dr_lee"));
        cm.addDoctor(new Doctor(5, "dr_kim"));

        cm.addAppointment(new Appointment(1, 1, 100, 30, AppointmentStatus.COMPLETED, AppointmentType.CONSULTATION));
        cm.addAppointment(new Appointment(2, 1, 101, 41, AppointmentStatus.COMPLETED, AppointmentType.CONSULTATION));
        cm.addAppointment(new Appointment(3, 1, 102, 20, AppointmentStatus.COMPLETED, AppointmentType.FOLLOWUP));
        // not COMPLETED -> excluded from averages
        cm.addAppointment(new Appointment(5, 1, 105, 100, AppointmentStatus.CANCELLED, AppointmentType.CONSULTATION));
        cm.addAppointment(new Appointment(8, 1, 200, 220, AppointmentStatus.SCHEDULED, AppointmentType.CONSULTATION));
        cm.addAppointment(new Appointment(4, 2, 103, 40, AppointmentStatus.COMPLETED, AppointmentType.EMERGENCY));
        cm.addAppointment(new Appointment(6, 4, 106, 25, AppointmentStatus.COMPLETED, AppointmentType.CONSULTATION));
        cm.addAppointment(new Appointment(7, 5, 107, 15, AppointmentStatus.COMPLETED, AppointmentType.FOLLOWUP));

        Map<AppointmentType, Double> avg1 = cm.getAverageAppointmentDurationByType(1);
        assert Math.abs(35.5 - avg1.get(AppointmentType.CONSULTATION)) < 1e-4 : 
        "Expected 35.5 for CONSULTATION";  // (30+41)/2
        
        assert Math.abs(20.0 - avg1.get(AppointmentType.FOLLOWUP)) < 1e-4 : "Expected 20.0 for FOLLOWUP";
        assert !avg1.containsKey(AppointmentType.EMERGENCY) : "EMERGENCY should not be in avg1";

        Map<AppointmentType, Double> avg2 = cm.getAverageAppointmentDurationByType(2);
        assert Math.abs(40.0 - avg2.get(AppointmentType.EMERGENCY)) < 1e-4 : "Expected 40.0 for EMERGENCY";
        assert !avg2.containsKey(AppointmentType.CONSULTATION) : "CONSULTATION should not be in avg2";
        assert !avg2.containsKey(AppointmentType.FOLLOWUP) : "FOLLOWUP should not be in avg2";

        Map<AppointmentType, Double> avg4 = cm.getAverageAppointmentDurationByType(4);
        assert Math.abs(25.0 - avg4.get(AppointmentType.CONSULTATION)) < 1e-4 : "Expected 25.0 for CONSULTATION";
        assert !avg4.containsKey(AppointmentType.FOLLOWUP) : "FOLLOWUP should not be in avg4";
        assert !avg4.containsKey(AppointmentType.EMERGENCY) : "EMERGENCY should not be in avg4";

        Map<AppointmentType, Double> avg5 = cm.getAverageAppointmentDurationByType(5);
        assert Math.abs(15.0 - avg5.get(AppointmentType.FOLLOWUP)) < 1e-4 : "Expected 15.0 for FOLLOWUP";
        assert !avg5.containsKey(AppointmentType.CONSULTATION) : "CONSULTATION should not be in avg5";
        assert !avg5.containsKey(AppointmentType.EMERGENCY) : "EMERGENCY should not be in avg5";

        // doctor with no appointments
        assert cm.getAverageAppointmentDurationByType(3).isEmpty() : "Expected empty map for doctor with no appointments";

        // unknown doctorId -> empty map
        assert cm.getAverageAppointmentDurationByType(999).isEmpty() : "Expected empty map for unknown doctorId";
    }
}