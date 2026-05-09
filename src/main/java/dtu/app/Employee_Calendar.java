package dtu.app;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Employee_Calendar {
    String id;

    // Week-based calendar: key is ISO year-week string "YYYY-Www", e.g. "2026-W19"
    private Map<String, List<CalendarEntry>> calendar = new HashMap<>();

    private String toWeekKey(LocalDate date) {
        int year = date.get(WeekFields.ISO.weekBasedYear());
        int week = date.get(WeekFields.ISO.weekOfWeekBasedYear());
        return String.format("%d-W%02d", year, week);
    }

    // Returns the set of distinct ISO week keys covered by [startDate, endDate)
    private Set<String> distinctWeeks(LocalDate startDate, LocalDate endDate) {
        Set<String> weeks = new LinkedHashSet<>();
        int days = (int) startDate.until(endDate, ChronoUnit.DAYS);
        for (int i = 0; i < days; i++) {
            weeks.add(toWeekKey(startDate.plusDays(i)));
        }
        return weeks;
    }

    // Pre-populates 13 weeks so getEntries() always returns an empty list (not null)
    // for weeks within that window, even before any activity is registered
    public Employee_Calendar(String Employee) {
        this.id = Employee;
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 13; i++) {
            calendar.put(toWeekKey(today.plusWeeks(i)), new ArrayList<>());
        }
    }

    // Enforces a soft cap of 10 activities per week; throws so the caller can
    // prompt the user for confirmation before calling forceRegisterActivity
    public void registerActivity(LocalDate date, String activity) {
        if (getEntries(date).size() > 9) {
            throw new IllegalArgumentException("are you sure this activity should be added, schedule is full");
        } else {
            calendar.computeIfAbsent(toWeekKey(date), k -> new ArrayList<>())
                    .add(new CalendarEntry(CalendarEntryType.ACTIVITY, activity));
        }
    }

    // Bypasses the 10-activity limit — use only when user has explicitly confirmed
    public void forceRegisterActivity(LocalDate date, String activity) {
        calendar.computeIfAbsent(toWeekKey(date), k -> new ArrayList<>())
                .add(new CalendarEntry(CalendarEntryType.ACTIVITY, activity));
    }

    // Range version of forceRegisterActivity — bypasses the cap for all weeks in the period
    public void forceRegisterActivity(LocalDate startDate, LocalDate endDate, String activity) {
        for (String key : distinctWeeks(startDate, endDate)) {
            calendar.computeIfAbsent(key, k -> new ArrayList<>())
                    .add(new CalendarEntry(CalendarEntryType.ACTIVITY, activity));
        }
    }

    // Registers one entry per distinct week covered by [startDate, endDate)
    public void registerActivity(LocalDate startDate, LocalDate endDate, String activity) {
        for (String key : distinctWeeks(startDate, endDate)) {
            calendar.computeIfAbsent(key, k -> new ArrayList<>())
                    .add(new CalendarEntry(CalendarEntryType.ACTIVITY, activity));
        }
    }

    // Register sickness or time off in the week containing date
    public void registerTimeOff(LocalDate date, String type) {
        calendar.computeIfAbsent(toWeekKey(date), k -> new ArrayList<>())
                .add(new CalendarEntry(CalendarEntryType.TIME_OFF, type));
    }

    // Register time off for all weeks in [startDate, endDate) — endDate is exclusive
    public void registerTimeOff(LocalDate startDate, LocalDate endDate, String type) {
        for (String key : distinctWeeks(startDate, endDate)) {
            calendar.computeIfAbsent(key, k -> new ArrayList<>())
                    .add(new CalendarEntry(CalendarEntryType.TIME_OFF, type));
        }
    }

    // Returns true if any week in [startDate, endDate) has a TIME_OFF entry
    public boolean hasTimeOffInPeriod(LocalDate startDate, LocalDate endDate) {
        for (String key : distinctWeeks(startDate, endDate)) {
            for (CalendarEntry entry : calendar.getOrDefault(key, Collections.emptyList())) {
                if (entry.getType() == CalendarEntryType.TIME_OFF) {
                    return true;
                }
            }
        }
        return false;
    }

    // Returns false if any week in [startDate, endDate) is already at the 10-activity cap
    public boolean isAvailableForPeriod(LocalDate startDate, LocalDate endDate) {
        for (String key : distinctWeeks(startDate, endDate)) {
            if (calendar.getOrDefault(key, Collections.emptyList()).size() >= 10) {
                return false;
            }
        }
        return true;
    }

    // Get all entries for the week containing date
    public List<CalendarEntry> getEntries(LocalDate date) {
        return calendar.getOrDefault(toWeekKey(date), Collections.emptyList());
    }

    // Get all entries across all distinct weeks in [startDate, endDate)
    public List<CalendarEntry> getEntries(LocalDate startDate, LocalDate endDate) {
        List<CalendarEntry> entries = new ArrayList<>();
        for (String key : distinctWeeks(startDate, endDate)) {
            entries.addAll(calendar.getOrDefault(key, Collections.emptyList()));
        }
        return entries;
    }

    // Removes the most-recently-added entry in the week containing date (LIFO)
    public void removeActivity(String date) {
        List<CalendarEntry> entries = calendar.get(toWeekKey(LocalDate.parse(date)));
        if (entries != null) {
            entries.removeLast();
        }
    }

    public void removeAllActivities(String date) {
        List<CalendarEntry> entries = calendar.get(toWeekKey(LocalDate.parse(date)));
        if (entries != null) {
            entries.clear();
        }
    }

    public void removeActivity(LocalDate date, String activity) {
        List<CalendarEntry> entries = calendar.get(toWeekKey(date));
        if (entries != null) {
            entries.removeIf(e -> e.getDescription().equals(activity));
        }
    }

    public void removeActivity(LocalDate startDate, LocalDate endDate, String activity) {
        for (String key : distinctWeeks(startDate, endDate)) {
            List<CalendarEntry> entries = calendar.get(key);
            if (entries != null) {
                entries.removeIf(e -> e.getDescription().equals(activity));
            }
        }
    }

    // Only replaces the first matching entry in that week; silently does nothing if not found
    public void changeActivity(LocalDate date, String oldActivity, String newActivity) {
        List<CalendarEntry> entries = calendar.get(toWeekKey(date));
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
        for (String key : distinctWeeks(startDate, endDate)) {
            List<CalendarEntry> entries = calendar.get(key);
            if (entries != null) {
                entries.clear();
            }
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
