package dtu.app;

import java.util.*;
import java.time.LocalDate;

public class employee_Calendar {
    String id;
    Calendar earliest_Date_In_Calendar;

    // Unified calendar: each date maps to a list of CalendarEntry (activity or time
    // off)
    private Map<LocalDate, List<CalendarEntry>> calendar = new HashMap<>();

    public employee_Calendar(String Employee) {
        this.id = Employee;
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 90; i++) {
            LocalDate date = today.plusDays(i);
            calendar.put(date, new ArrayList<>());
        }
    }

    // Register an activity on a specific date
    public void registerActivity(LocalDate date, String activity) {
        calendar.computeIfAbsent(date, k -> new ArrayList<>())
                .add(new CalendarEntry(CalendarEntryType.ACTIVITY, activity));
    }

    // Register sickness or time off on a specific date
    public void registerTimeOff(LocalDate date, String type) {
        calendar.computeIfAbsent(date, k -> new ArrayList<>()).add(new CalendarEntry(CalendarEntryType.TIME_OFF, type));
    }

    // Get all entries for a specific date
    public List<CalendarEntry> getEntries(LocalDate date) {
        return calendar.getOrDefault(date, Collections.emptyList());
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
