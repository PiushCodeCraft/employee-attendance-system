package services;

import models.Attendance;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class AttendanceService {

    private static final LocalTime LATE_CUTOFF = LocalTime.of(9, 30);
    private static Map<Integer, LocalTime> checkedInToday = new HashMap<>();

    private static List<Attendance> records = new ArrayList<>(Arrays.asList(
        new Attendance(1, 2, LocalDate.now().minusDays(1), LocalTime.of(9,0),  LocalTime.of(18,0), "Present", 9.0),
        new Attendance(2, 2, LocalDate.now().minusDays(2), LocalTime.of(9,45), LocalTime.of(18,0), "Late",    8.25),
        new Attendance(3, 2, LocalDate.now().minusDays(3), LocalTime.of(9,5),  LocalTime.of(17,30),"Present", 8.42),
        new Attendance(4, 2, LocalDate.now().minusDays(4), LocalTime.of(9,0),  LocalTime.of(18,0), "Present", 9.0),
        new Attendance(5, 2, LocalDate.now().minusDays(5), null, null,                             "Absent",  0.0),
        new Attendance(6, 3, LocalDate.now().minusDays(1), LocalTime.of(8,55), LocalTime.of(17,55),"Present", 9.0),
        new Attendance(7, 3, LocalDate.now().minusDays(2), LocalTime.of(10,0), LocalTime.of(18,0), "Late",    8.0),
        new Attendance(8, 4, LocalDate.now().minusDays(1), LocalTime.of(9,0),  LocalTime.of(18,0), "Present", 9.0),
        new Attendance(9, 5, LocalDate.now().minusDays(1), LocalTime.of(9,20), LocalTime.of(17,30),"Present", 8.17)
    ));

    private static int nextId = 10;

    public String checkIn(int empId) {
        if (checkedInToday.containsKey(empId)) return "Already checked in today!";
        LocalTime now = LocalTime.now();
        String status = now.isAfter(LATE_CUTOFF) ? "Late" : "Present";
        checkedInToday.put(empId, now);
        Attendance a = new Attendance(nextId++, empId, LocalDate.now(), now, null, status, 0);
        records.add(a);
        return "Check-in successful! Status: " + status;
    }

    public String checkOut(int empId) {
        if (!checkedInToday.containsKey(empId)) return "You have not checked in today!";
        LocalTime now = LocalTime.now();
        for (Attendance a : records) {
            if (a.getEmpId() == empId && a.getDate().equals(LocalDate.now()) && a.getCheckOut() == null) {
                a.setCheckOut(now);
                long mins = java.time.Duration.between(a.getCheckIn(), now).toMinutes();
                a.setWorkingHours(Math.round(mins / 60.0 * 100.0) / 100.0);
            }
        }
        checkedInToday.remove(empId);
        return "Check-out successful!";
    }

    public boolean hasCheckedInToday(int empId) {
        return checkedInToday.containsKey(empId);
    }

    public List<Attendance> getMyAttendance(int empId) {
        List<Attendance> list = new ArrayList<>();
        for (Attendance a : records) if (a.getEmpId() == empId) list.add(a);
        list.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        return list;
    }

    public List<Attendance> getAllAttendance() {
        List<Attendance> list = new ArrayList<>(records);
        list.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        return list;
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        List<Attendance> list = new ArrayList<>();
        for (Attendance a : records) if (a.getDate().equals(date)) list.add(a);
        return list;
    }
}
