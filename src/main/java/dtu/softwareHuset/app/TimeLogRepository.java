package dtu.softwareHuset.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

// Handles all CSV read/write operations for time log entries in timeLog.csv.
// Project/activity metadata (dates, descriptions, employees) lives in projectSaves.csv.
// Column layout (0-indexed):
//   0  entryId   1  employeeId   2  projectId   3  activityName   4  date   5  hours
public class TimeLogRepository {

    private static final String COMMA_DELIMITER = ",";
    private static final File file = new File("data/timeLog.csv");
    private static final String HEADER = "entryId,employeeId,projectId,activityName,date,hours";

    // Reads all non-header, non-blank rows from the CSV and returns them as a list
    // of string lists. Each inner list maps 1-to-1 with the CSV columns above.
    // Returns an empty list when the file does not exist yet.
    // Author: GubbeMK
    public List<List<String>> load() throws IOException {
        List<List<String>> logs = new ArrayList<>();
        if (!file.exists()) return logs;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; }
                if (line.isBlank()) continue;
                String[] entries = line.split(COMMA_DELIMITER);
                logs.add(Arrays.asList(entries));
            }
        }

        return logs;
    }

    // Writes a new real time-log entry to the CSV by appending a new row.
    // Author: GubbeMK
    public void registerEntry(TimeLog entry) throws IOException {
        List<List<String>> existing = load();
        file.getParentFile().mkdirs();

        String row = (existing.size() + 1) + COMMA_DELIMITER + entry.toString();
        existing.add(new ArrayList<>(Arrays.asList(row.split(COMMA_DELIMITER))));

        writeAll(existing);
    }

    // Sorts all rows by projectId then activityId and writes them to the CSV,
    // always prefixing with the standard header row.
    // Author: GubbeMK
    public void writeAll(List<List<String>> logs) throws IOException {
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(HEADER);
            bw.newLine();

            for (List<String> log : logs) {
                bw.write(String.join(COMMA_DELIMITER, log));
                bw.newLine();
            }
        }
    }

    // // Prints every entry logged by the given employee today, along with the total
    // // hours. Used for quick console inspection.
    // // Author: GubbeMK
    // public void hoursLoggedToday(Employee employee) throws IOException {
    //     List<List<String>> logs = new ArrayList<>();
    //     double totalHours = 0;

    //     try (BufferedReader br = new BufferedReader(new FileReader(file))) {
    //         String line;
    //         boolean isHeader = true;
    //         while ((line = br.readLine()) != null) {
    //             if (isHeader) {
    //                 isHeader = false;
    //                 continue;
    //             }

    //             String[] entries = line.split(COMMA_DELIMITER);
    //             if (entries[1].equals(employee.getInitials()) && LocalDate.parse(entries[4]).equals(LocalDate.now())) {
    //                 logs.add(Arrays.asList(entries));
    //             }
    //         }
    //     }

    //     System.out.println("Logs from today: ");
    //     for (List<String> log : logs) {
    //         System.out.println("\n" + log);
    //         totalHours += Double.valueOf(log.get(5));
    //     }

    //     System.out.println("Total hours worked today: " + totalHours);
    // }

    // // Returns the total hours logged by the given employee in the current calendar week.
    // // Author: GubbeMK
    // public double hoursLoggedWeek(Employee employee) throws IOException {
    //     List<List<String>> logs = new ArrayList<>();
    //     double totalHours = 0;

    //     // Resolve the current week number using the JVM's default locale
    //     LocalDate date = LocalDate.now();
    //     TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
    //     int weekNumber = date.get(woy);

    //     try (BufferedReader br = new BufferedReader(new FileReader(file))) {
    //         String line;
    //         boolean isHeader = true;
    //         while ((line = br.readLine()) != null) {
    //             if (isHeader) {
    //                 isHeader = false;
    //                 continue;
    //             }

    //             String[] entries = line.split(COMMA_DELIMITER);
    //             if (entries[1].equals(employee.getInitials()) && LocalDate.parse(entries[4]).get(woy) == weekNumber) {
    //                 logs.add(Arrays.asList(entries));
    //             }
    //         }
    //     }

    //     System.out.println("Logs from this week: ");
    //     for (List<String> log : logs) {
    //         totalHours += Double.valueOf(log.get(5));
    //     }

    //     return totalHours;
    // }

    // // Returns the total hours logged by the given employee in the current calendar month.
    // // Author: GubbeMK
    // public double hoursLoggedMonth(Employee employee) throws IOException {
    //     List<List<String>> logs = new ArrayList<>();
    //     double totalHours = 0;

    //     try (BufferedReader br = new BufferedReader(new FileReader(file))) {
    //         String line;
    //         boolean isHeader = true;
    //         while ((line = br.readLine()) != null) {
    //             if (isHeader) {
    //                 isHeader = false;
    //                 continue;
    //             }

    //             String[] entries = line.split(COMMA_DELIMITER);
    //             LocalDate entryDate = LocalDate.parse(entries[4]);
    //             LocalDate today = LocalDate.now();
    //             // Match both the year and the month to stay within the current calendar month
    //             if (entries[1].equals(employee.getInitials()) && entryDate.getYear() == today.getYear()
    //                     && entryDate.getMonth() == today.getMonth()) {
    //                 logs.add(Arrays.asList(entries));
    //             }
    //         }
    //     }

    //     for (List<String> log : logs) {
    //         totalHours += Double.valueOf(log.get(5));
    //     }

    //     return totalHours;
    // }

    // Replaces the row at the given zero-based index with newValues.
    // Used for direct positional edits where the caller already knows the row index.
    // Author: GubbeMK
    public void editEntry(int lineIndex, List<String> newValues) throws IOException {
        List<List<String>> logs = load();
        logs.set(lineIndex, newValues);
        writeAll(logs);
    }

    // Finds the row whose entryId matches the given id and replaces it with the
    // data from newEntry, keeping the same entryId in column 0.
    // Author: GubbeMK
    public void updateEntry(String entryId, TimeLog newEntry) throws IOException {
        List<List<String>> logs = load();
        String row = entryId + COMMA_DELIMITER + newEntry.toString();
        List<String> editedLog = Arrays.asList(row.split(COMMA_DELIMITER));

        for (int i = 0; i < logs.size(); i++) {
            if (logs.get(i).get(0).equals(entryId)) {
                logs.set(i, editedLog);
                break;
            }
        }

        writeAll(logs);
    }

    // Returns the row at the given zero-based index from the loaded log list.
    // Author: GubbeMK
    public List<String> getEntry(int logNumber) throws IOException {
        List<List<String>> logs = load();
        return logs.get(logNumber);
    }

    // Removes the row with the given entryId and decrements the id of every row
    // with a higher id by 1, keeping the sequence gap-free.
    // Author: GLS0712
    public void deleteEntry(String entryId) throws IOException {
        List<List<String>> logs = load();
        logs.removeIf(row -> !row.isEmpty() && row.get(0).equals(entryId));

        int deletedId;
        try {
            deletedId = Integer.parseInt(entryId);
        } catch (NumberFormatException e) {
            // entryId was not numeric — just write the filtered list as-is
            writeAll(logs);
            return;
        }

        // Shift down every id that was above the deleted one
        for (int i = 0; i < logs.size(); i++) {
            List<String> row = logs.get(i);
            if (!row.isEmpty()) {
                try {
                    int id = Integer.parseInt(row.get(0));
                    if (id > deletedId) {
                        List<String> mutable = new ArrayList<>(row);
                        mutable.set(0, String.valueOf(id - 1));
                        logs.set(i, mutable);
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        writeAll(logs);
    }

    // Removes all rows belonging to the given project. Called when a project is deleted.
    // Author: GLS0712
    public void deleteEntriesForProject(String projectId) throws IOException {
        List<List<String>> logs = load();
        logs.removeIf(row -> row.size() > 2 && row.get(2).equals(projectId));
        writeAll(logs);
    }

    // Removes all rows for the given activity (matched by name). Called when an activity is deleted.
    // Author: GLS0712
    public void deleteEntriesForActivity(String activityName) throws IOException {
        List<List<String>> logs = load();
        logs.removeIf(row -> row.size() > 3 && row.get(3).equals(activityName));
        writeAll(logs);
    }

}
