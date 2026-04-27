package dtu.app;

import java.util.*;

import dtu.app.Employee_Calendar.CalendarEntry;
import dtu.app.Employee_Calendar.CalendarEntryType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Employee_Calendar {
    String id;
    Calendar earliest_Date_In_Calendar;

    // Unified calendar: each date maps to a list of CalendarEntry (activity or time
    // off)
    private Map<LocalDate, List<CalendarEntry>> calendar = new HashMap<>();

    public Employee_Calendar(String Employee) {
        this.id = Employee;
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 90; i++) {
            LocalDate date = today.plusDays(i);
            calendar.put(date, new ArrayList<>());
        }
    }

    // Register an activity on a specific date
    public void registerActivity(LocalDate date, String activity) {
        if (getEntries(date).size() > 9) {
            throw new IllegalArgumentException("are you sure this activity should be added, schedule is full");
        } else {
            calendar.computeIfAbsent(date, k -> new ArrayList<>())
                    .add(new CalendarEntry(CalendarEntryType.ACTIVITY, activity));
        }
    }

    // Bypasses the 10-activity limit — use only when user has explicitly confirmed
    public void forceRegisterActivity(LocalDate date, String activity) {
        calendar.computeIfAbsent(date, k -> new ArrayList<>())
                .add(new CalendarEntry(CalendarEntryType.ACTIVITY, activity));
    }

    // Register an activity over multible dates
    public void registerActivity(LocalDate startDate, LocalDate endDate, String activity) {
        int periode = (int) startDate.until(endDate, ChronoUnit.DAYS);
        for (int i = 0; i < periode; i++) {
            calendar.computeIfAbsent(startDate.plusDays(i), k -> new ArrayList<>())
                    .add(new CalendarEntry(CalendarEntryType.ACTIVITY, activity));
        }
    }

    // Register sickness or time off on a specific date
    public void registerTimeOff(LocalDate date, String type) {
        calendar.computeIfAbsent(date, k -> new ArrayList<>()).add(new CalendarEntry(CalendarEntryType.TIME_OFF, type));
    }

    // Get all entries for a specific date
    public List<CalendarEntry> getEntries(LocalDate date) {
        return calendar.getOrDefault(date, Collections.emptyList());
    }

    // Get all entries over multiple dates
    public List<CalendarEntry> getEntries(LocalDate startDate, LocalDate endDate) {
        int periode = (int) startDate.until(endDate, ChronoUnit.DAYS);
        List<CalendarEntry> entries = new ArrayList<>();
        for (int i = 0; i < periode; i++) {
            entries.addAll(calendar.getOrDefault(startDate.plusDays(i), Collections.emptyList()));
        }
        return entries;
    }

    // Helper class for calendar entries
    public static class CalendarEntry {
        private final CalendarEntryType type;
        private final String description;

        public CalendarEntry(CalendarEntryType type, String description) {
            this.type = type;
            this.description = description;
        }

        public CalendarEntryType getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum CalendarEntryType {
        ACTIVITY,
        TIME_OFF
    }
}
