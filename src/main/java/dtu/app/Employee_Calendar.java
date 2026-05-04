package dtu.app;

import java.util.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Employee_Calendar {
    String id;
    Calendar earliest_Date_In_Calendar;

    // Unified calendar: each date maps to a list of CalendarEntry (activity or time off)
    private Map<LocalDate, List<CalendarEntry>> calendar = new HashMap<>();

    // Pre-populates 90 days so getEntries() always returns an empty list (not null)
    // for dates within that window, even before any activity is registered
    public Employee_Calendar(String Employee) {
        this.id = Employee;
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 90; i++) {
            LocalDate date = today.plusDays(i);
            calendar.put(date, new ArrayList<>());
        }
    }

    // Enforces a soft cap of 10 activities per day; throws so the caller can
    // prompt the user for confirmation before calling forceRegisterActivity
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

    // endDate is exclusive: a range of 2026-01-01 to 2026-01-03 covers only 01 and 02
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

    // Removes the most-recently-added entry on that date (LIFO), not by name
    public void removeActivity(String date) {
        List<CalendarEntry> entries = calendar.get(LocalDate.parse(date));
        if (entries != null) {
            entries.removeLast();
        }
    }

    public void removeAllActivities(String date) {
        List<CalendarEntry> entries = calendar.get(LocalDate.parse(date));
        if (entries != null) {
            entries.clear();
        }
    }

    public void removeActivity(LocalDate date, String activity) {
        List<CalendarEntry> entries = calendar.get(date);
        if (entries != null) {
            entries.removeIf(e -> e.getDescription().equals(activity));
        }
    }

    public void removeActivity(LocalDate startDate, LocalDate endDate, String activity) {
        int periode = (int) startDate.until(endDate, ChronoUnit.DAYS);
        for (int i = 0; i < periode; i++) {
            removeActivity(startDate.plusDays(i), activity);
        }
    }

    // Only replaces the first matching entry; silently does nothing if not found
    public void changeActivity(LocalDate date, String oldActivity, String newActivity) {
        List<CalendarEntry> entries = calendar.get(date);
        if (entries != null) {
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).getDescription().equals(oldActivity)) {
                    entries.set(i, new CalendarEntry(entries.get(i).getType(), newActivity));
                    return;
                }
            }
        }
    }

    public void removeAllActivities(LocalDate startDate, LocalDate endDate) {
        int periode = (int) startDate.until(endDate, ChronoUnit.DAYS);
        for (int i = 0; i < periode; i++) {
            removeAllActivities(startDate.plusDays(i).toString());
        }
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
