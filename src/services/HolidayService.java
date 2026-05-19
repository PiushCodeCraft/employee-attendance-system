package services;

import models.Holiday;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HolidayService {

    private static List<Holiday> holidays = new ArrayList<>(Arrays.asList(
        new Holiday(1, "Republic Day",      LocalDate.of(2026, 1, 26)),
        new Holiday(2, "Holi",              LocalDate.of(2026, 3, 14)),
        new Holiday(3, "Independence Day",  LocalDate.of(2026, 8, 15)),
        new Holiday(4, "Gandhi Jayanti",    LocalDate.of(2026, 10, 2)),
        new Holiday(5, "Diwali",            LocalDate.of(2026, 10, 20)),
        new Holiday(6, "Christmas",         LocalDate.of(2026, 12, 25))
    ));

    private static int nextId = 7;

    // All holidays
    public List<Holiday> getAll() {
        return new ArrayList<>(holidays);
    }

    // Upcoming (today or future), sorted by date
    public List<Holiday> getUpcoming() {
        LocalDate today = LocalDate.now();
        List<Holiday> list = new ArrayList<>();
        for (Holiday h : holidays)
            if (!h.getDate().isBefore(today)) list.add(h);
        list.sort((a, b) -> a.getDate().compareTo(b.getDate()));
        return list;
    }

    // Add a holiday
    public void add(String name, LocalDate date) {
        holidays.add(new Holiday(nextId++, name, date));
    }

    // Remove by id
    public boolean remove(int id) {
        return holidays.removeIf(h -> h.getId() == id);
    }

    // Check if a date is a holiday
    public boolean isHoliday(LocalDate date) {
        return holidays.stream().anyMatch(h -> h.getDate().equals(date));
    }
}