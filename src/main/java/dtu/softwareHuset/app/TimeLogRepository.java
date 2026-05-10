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

public class TimeLogRepository {

    private static final String COMMA_DELIMITER = ",";
    private static final File file = new File("data/timeLog.csv");
    private static final String HEADER = "entryId,employeeId,projectId,activityName,date,hours";

    public List<List<String>> load() throws IOException {
        List<List<String>> logs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] entries = line.split(COMMA_DELIMITER);
                logs.add(Arrays.asList(entries));
            }
        }

        return logs;
    }

    public void registerEntry(TimeLog entry) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write((load().size() + 1) + COMMA_DELIMITER + entry.toString());
            bw.newLine();
        }
    }

    public void writeAll(List<List<String>> logs) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(HEADER);
            bw.newLine();

            for (List<String> log : logs) {
                bw.write(String.join(COMMA_DELIMITER, log));
                bw.newLine();
            }
        }
    }

    public void preview() throws IOException {
        List<List<String>> logs = load();

        System.out.println("\nLoaded " + logs.size() + " logs:");
        for (List<String> log : logs) {
            System.out.println(log);
        }
    }

    public void hoursLoggedToday(Employee employee) throws IOException {
        List<List<String>> logs = new ArrayList<>();
        double totalHours = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] entries = line.split(COMMA_DELIMITER);
                if (entries[1].equals(employee.getInitials()) && LocalDate.parse(entries[4]).equals(LocalDate.now())) {
                    logs.add(Arrays.asList(entries));
                }
            }
        }

        System.out.println("Logs from today: ");
        for (List<String> log : logs) {
            System.out.println("\n" + log);
            totalHours += Double.valueOf(log.get(4));
        }

        System.out.println("Total hours worked today: " + totalHours);
    }

    public double hoursLoggedWeek(Employee employee) throws IOException {
        List<List<String>> logs = new ArrayList<>();
        double totalHours = 0;

        LocalDate date = LocalDate.now();
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = date.get(woy);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] entries = line.split(COMMA_DELIMITER);
                if (entries[1].equals(employee.getInitials()) && LocalDate.parse(entries[4]).get(woy) == weekNumber) {
                    logs.add(Arrays.asList(entries));
                }
            }
        }

        System.out.println("Logs from this week: ");
        for (List<String> log : logs) {
            totalHours += Double.valueOf(log.get(4));
        }

        return totalHours;
    }

    public double hoursLoggedMonth(Employee employee) throws IOException {
        List<List<String>> logs = new ArrayList<>();
        double totalHours = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] entries = line.split(COMMA_DELIMITER);
                LocalDate entryDate = LocalDate.parse(entries[4]);
                LocalDate today = LocalDate.now();
                if (entries[1].equals(employee.getInitials()) && entryDate.getYear() == today.getYear()
                        && entryDate.getMonth() == today.getMonth()) {
                    logs.add(Arrays.asList(entries));
                }
            }
        }

        for (List<String> log : logs) {
            totalHours += Double.valueOf(log.get(4));
        }

        return totalHours;
    }

    public void editEntry(int lineIndex, List<String> newValues) throws IOException {
        List<List<String>> logs = load();
        logs.set(lineIndex, newValues);
        writeAll(logs);
    }

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

    public List<String> getEntry(int logNumber) throws IOException {
        List<List<String>> logs = load();
        return logs.get(logNumber);
    }
}
