package dtu.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static void writeAll(List<List<String>> logs) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            int lineNumber = 0;
            bw.write(HEADER);
            bw.newLine();
            for (List<String> log : logs) {
                lineNumber++;
                bw.write(lineNumber);
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

    public void editEntry(int lineIndex, List<String> newValues) throws IOException {
        List<List<String>> logs = load();

        if (lineIndex < 0 || lineIndex >= logs.size()) {
            System.out.println("HEJ");
            throw new IllegalArgumentException("Line index out of range: " + lineIndex);
        }

        logs.set(lineIndex, newValues);
        writeAll(logs);
    }

    public List<String> getEntry(int logNumber) throws IOException {
        List<List<String>> logs = load();
        return logs.get(logNumber);
    }
}
