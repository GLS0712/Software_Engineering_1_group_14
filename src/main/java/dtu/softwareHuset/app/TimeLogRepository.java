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
import java.util.stream.Collectors;

// Single source of truth for all CSV read/write operations on timeLog.csv.
// Column layout (0-indexed):
//   0  entryId          1  employeeId        2  employeeLoggedTime  3  loggedTimeDate
//   4  projectId        5  projectLeader     6  projectName         7  activityId
//   8  activityName     9  projectStartDate  10 projectEndDate      11 activityStartDate
//  12  activityEndDate  13 allottedTime      14 projectDescription  15 activityDescription
//  16  employees (dash-separated initials)
public class TimeLogRepository {

    private static final String COMMA_DELIMITER = ",";
    private static final File file = new File("data/timeLog.csv");
    private static final String HEADER = "entryId,employeeId,employeeLoggedTime,loggedTimeDate,projectId,projectLeader,projectName,activityId,activityName,projectStartDate,projectEndDate,activityStartDate,activityEndDate,Alloted time,projectDescription,activityDescription,employees";

    // Reads all non-header, non-blank rows from the CSV and returns them as a list
    // of string lists. Each inner list maps 1-to-1 with the CSV columns above.
    // Returns an empty list when the file does not exist yet.
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

    // Writes a new real time-log entry to the CSV.
    // If the activity already has a stub row (empty employeeId), that row is filled
    // in-place so no duplicate rows accumulate for the first log on an activity.
    // A new row is only appended when the activity already has at least one real entry.
    public void registerEntry(TimeLog entry) throws IOException {
        List<List<String>> existing = load();
        file.getParentFile().mkdirs();

        String projectId  = entry.project.getId();
        String activityId = entry.activity.getId();

        // Search for a stub row matching this project+activity that has no employee yet
        int stubIndex = -1;
        for (int i = 0; i < existing.size(); i++) {
            List<String> row = existing.get(i);
            if (row.size() > 7
                    && row.get(4).equals(projectId)
                    && row.get(7).equals(activityId)
                    && row.get(1).isEmpty()) {
                stubIndex = i;
                break;
            }
        }

        if (stubIndex >= 0) {
            // Fill the stub's employee/hours/date columns without touching the rest
            List<String> stub = new ArrayList<>(existing.get(stubIndex));
            while (stub.size() <= 16) stub.add("");
            stub.set(1, entry.employee.getInitials());
            stub.set(2, entry.hours.toString());
            stub.set(3, entry.date.toString());
            existing.set(stubIndex, stub);
        } else {
            // No stub found — append a fully-populated new row
            String row = (existing.size() + 1) + COMMA_DELIMITER + entry.toString();
            existing.add(new ArrayList<>(Arrays.asList(row.split(COMMA_DELIMITER))));
        }

        writeAll(existing);
    }

    // Sorts all rows by projectId then activityId and writes them to the CSV,
    // always prefixing with the standard header row.
    public void writeAll(List<List<String>> logs) throws IOException {
        // Sort so the file stays grouped by project and then by activity within each project
        logs.sort((a, b) -> {
            String pid1 = a.size() > 4 ? a.get(4) : "";
            String pid2 = b.size() > 4 ? b.get(4) : "";
            int cmp = pid1.compareTo(pid2);
            if (cmp != 0) return cmp;
            String aid1 = a.size() > 7 ? a.get(7) : "";
            String aid2 = b.size() > 7 ? b.get(7) : "";
            return aid1.compareTo(aid2);
        });
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

    // Prints all loaded rows to stdout — used for quick debugging.
    public void preview() throws IOException {
        List<List<String>> logs = load();

        System.out.println("\nLoaded " + logs.size() + " logs:");
        for (List<String> log : logs) {
            System.out.println(log);
        }
    }

    // Prints every entry logged by the given employee today, along with the total
    // hours. Used for quick console inspection.
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
                if (entries[1].equals(employee.getInitials()) && LocalDate.parse(entries[3]).equals(LocalDate.now())) {
                    logs.add(Arrays.asList(entries));
                }
            }
        }

        System.out.println("Logs from today: ");
        for (List<String> log : logs) {
            System.out.println("\n" + log);
            totalHours += Double.valueOf(log.get(2));
        }

        System.out.println("Total hours worked today: " + totalHours);
    }

    // Returns the total hours logged by the given employee in the current calendar week.
    public double hoursLoggedWeek(Employee employee) throws IOException {
        List<List<String>> logs = new ArrayList<>();
        double totalHours = 0;

        // Resolve the current week number using the JVM's default locale
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
                if (entries[1].equals(employee.getInitials()) && LocalDate.parse(entries[3]).get(woy) == weekNumber) {
                    logs.add(Arrays.asList(entries));
                }
            }
        }

        System.out.println("Logs from this week: ");
        for (List<String> log : logs) {
            totalHours += Double.valueOf(log.get(2));
        }

        return totalHours;
    }

    // Returns the total hours logged by the given employee in the current calendar month.
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
                LocalDate entryDate = LocalDate.parse(entries[3]);
                LocalDate today = LocalDate.now();
                // Match both the year and the month to stay within the current calendar month
                if (entries[1].equals(employee.getInitials()) && entryDate.getYear() == today.getYear()
                        && entryDate.getMonth() == today.getMonth()) {
                    logs.add(Arrays.asList(entries));
                }
            }
        }

        for (List<String> log : logs) {
            totalHours += Double.valueOf(log.get(2));
        }

        return totalHours;
    }

    // Writes a placeholder (stub) row for a project or activity that has no logged
    // time yet. Columns 1-3 (employee, hours, date) are left empty.
    // When activity is null, only project-level columns are filled (used when a new
    // project is created). When activity is non-null, the first activity added to a
    // project fills the existing project-only stub instead of creating a new row.
    public void registerStubEntry(Project project, Activity activity) throws IOException {
        List<List<String>> existing = load();
        file.getParentFile().mkdirs();

        // Resolve all column values, defaulting to empty string when data is absent
        String projectLeader      = project.getProjectLeader() != null ? project.getProjectLeader().getInitials() : "";
        String projectStartDate   = project.getStartDate() != null ? project.getStartDate().toString() : "";
        String projectEndDate     = project.getEndDate() != null ? project.getEndDate() : "";
        String activityId         = activity != null ? activity.getId() : "";
        String activityName       = activity != null ? activity.getName() : "";
        String activityStartDate  = (activity != null && activity.getStartDate() != null) ? activity.getStartDate().toString() : "";
        String activityEndDate    = (activity != null && activity.getEndDate() != null) ? activity.getEndDate().toString() : "";
        String allottedTime       = (activity != null && activity.getAlottedTime() != null) ? activity.getAlottedTime() : "";
        String activityDescription = (activity != null && activity.getDescription() != null) ? activity.getDescription() : "";
        String projectDescription = project.getDescription() != null ? project.getDescription() : "";

        // When adding the first activity to a project, reuse the project-only stub row
        // (identified by a matching projectId with empty activityId and empty employeeId)
        // rather than appending a second row for the same project.
        if (activity != null) {
            for (int i = 0; i < existing.size(); i++) {
                List<String> row = existing.get(i);
                if (row.size() > 7
                        && row.get(4).equals(project.getId())
                        && row.get(7).isEmpty()
                        && row.get(1).isEmpty()) {
                    List<String> updated = new ArrayList<>(row);
                    while (updated.size() <= 16) updated.add("");
                    updated.set(7,  activityId);
                    updated.set(8,  activityName);
                    updated.set(11, activityStartDate);
                    updated.set(12, activityEndDate);
                    updated.set(13, allottedTime);
                    updated.set(15, activityDescription);
                    existing.set(i, updated);
                    writeAll(existing);
                    return;
                }
            }
        }

        // No matching stub found — build and append a new stub row.
        // Columns 1-3 are intentionally empty (no employee/hours/date yet).
        String row = (existing.size() + 1) + COMMA_DELIMITER
                + COMMA_DELIMITER                              // employeeId (empty)
                + COMMA_DELIMITER                              // employeeLoggedTime (empty)
                + COMMA_DELIMITER                              // loggedTimeDate (empty)
                + project.getId() + COMMA_DELIMITER
                + projectLeader + COMMA_DELIMITER
                + project.getName() + COMMA_DELIMITER
                + activityId + COMMA_DELIMITER
                + activityName + COMMA_DELIMITER
                + projectStartDate + COMMA_DELIMITER
                + projectEndDate + COMMA_DELIMITER
                + activityStartDate + COMMA_DELIMITER
                + activityEndDate + COMMA_DELIMITER
                + allottedTime + COMMA_DELIMITER
                + projectDescription + COMMA_DELIMITER
                + activityDescription + COMMA_DELIMITER
                + "";  // employees (empty)

        existing.add(new ArrayList<>(Arrays.asList(row.split(COMMA_DELIMITER))));
        writeAll(existing);
    }

    // Replaces the row at the given zero-based index with newValues.
    // Used for direct positional edits where the caller already knows the row index.
    public void editEntry(int lineIndex, List<String> newValues) throws IOException {
        List<List<String>> logs = load();
        logs.set(lineIndex, newValues);
        writeAll(logs);
    }

    // Finds the row whose entryId matches the given id and replaces it with the
    // data from newEntry, keeping the same entryId in column 0.
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
    public List<String> getEntry(int logNumber) throws IOException {
        List<List<String>> logs = load();
        return logs.get(logNumber);
    }

    // Removes the row with the given entryId and decrements the id of every row
    // with a higher id by 1, keeping the sequence gap-free.
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
    public void deleteEntriesForProject(String projectId) throws IOException {
        List<List<String>> logs = load();
        logs.removeIf(row -> row.size() > 4 && row.get(4).equals(projectId));
        writeAll(logs);
    }

    // Removes all rows belonging to the given activity. Called when an activity is deleted.
    public void deleteEntriesForActivity(String activityId) throws IOException {
        List<List<String>> logs = load();
        logs.removeIf(row -> row.size() > 7 && row.get(7).equals(activityId));
        writeAll(logs);
    }

    // Reconstructs in-memory Project and Activity objects from the CSV.
    // Projects and activities that already exist in the provided lists are skipped
    // so that the method is safe to call multiple times without creating duplicates.
    // Employee assignment to activities is also restored from the employees column.
    public void loadProjectsFromLogs(List<Project> projectList, List<Employee> employeeList) throws IOException {
        List<List<String>> logs = load();
        for (List<String> log : logs) {
            // Extract all columns, guarding against short rows produced by split()
            // dropping trailing empty strings
            String projectId           = log.get(4);
            String projectLeaderInit   = log.size() > 5  ? log.get(5)  : "";
            String projectName         = log.get(6);
            String activityId          = log.size() > 7  ? log.get(7)  : "";
            String activityName        = log.size() > 8  ? log.get(8)  : "";
            String projectStartDate    = log.size() > 9  ? log.get(9)  : "";
            String projectEndDate      = log.size() > 10 ? log.get(10) : "";
            String activityStartDate   = log.size() > 11 ? log.get(11) : "";
            String activityEndDate     = log.size() > 12 ? log.get(12) : "";
            String allottedTime        = log.size() > 13 ? log.get(13) : "";
            String projectDescription  = log.size() > 14 ? log.get(14) : "";
            String activityDescription = log.size() > 15 ? log.get(15) : "";
            String employeesStr        = log.size() > 16 ? log.get(16) : "";

            // Find or create the project
            Project project = projectList.stream()
                    .filter(p -> p.getName().equals(projectName))
                    .findFirst().orElse(null);
            if (project == null) {
                project = new Project(projectName);
                project.setId(projectId);
                projectList.add(project);
            }

            // Apply project-level fields only when not already set, so later rows
            // for the same project don't overwrite values set by an earlier row
            if (!projectLeaderInit.isEmpty() && project.getProjectLeader() == null) {
                for (Employee e : employeeList) {
                    if (projectLeaderInit.equals(e.getInitials())) { project.setProjectLeader(e); break; }
                }
            }
            if (!projectStartDate.isEmpty() && project.getStartDate() == null)
                project.setStartDate(LocalDate.parse(projectStartDate));
            if (!projectEndDate.isEmpty() && (project.getEndDate() == null || project.getEndDate().isEmpty()))
                project.setEndDate(projectEndDate);
            if (!projectDescription.isEmpty() && project.getDescription() == null)
                project.setDescription(projectDescription);

            // Create the activity if this row carries one and it does not exist yet
            if (!activityName.isEmpty() && project.getActivityFromName(activityName) == null) {
                Activity a = new Activity(activityName, activityDescription);
                a.setId(activityId);
                if (!activityStartDate.isEmpty()) a.setStartDate(LocalDate.parse(activityStartDate));
                if (!activityEndDate.isEmpty())   a.setEndDate(LocalDate.parse(activityEndDate));
                if (!allottedTime.isEmpty())      a.setAlottedTime(allottedTime);
                project.getActivities().add(a);
            }

            // Restore employee assignments stored as dash-separated initials
            if (!activityName.isEmpty() && !employeesStr.isEmpty()) {
                Activity activity = project.getActivityFromName(activityName);
                if (activity != null) {
                    for (String initials : employeesStr.split("-")) {
                        String trimmed = initials.trim();
                        for (Employee e : employeeList) {
                            if (trimmed.equals(e.getInitials()) && !activity.getEmployees().contains(e)) {
                                activity.getEmployees().add(e);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    // Pushes the current in-memory state of all projects and activities back into
    // the CSV. Only rows that can be matched to a known project and activity are
    // updated; unmatched rows (e.g. orphaned stubs) are left unchanged.
    public void syncLogs(List<Project> projects) throws IOException {
        List<List<String>> logs = load();
        for (int i = 0; i < logs.size(); i++) {
            List<String> log = new ArrayList<>(logs.get(i));

            // Look up the project this row belongs to by its stored projectId
            Project project = projects.stream()
                    .filter(p -> p.getId().equals(log.get(4)))
                    .findFirst().orElse(null);
            if (project == null) continue;

            // Rows with no activityId (project-only stubs) are skipped
            Activity activity = project.getActivityById(log.size() > 7 ? log.get(7) : "");
            if (activity == null) continue;

            // Rebuild the employee list as dash-separated initials
            String employees = activity.getEmployees().stream()
                    .map(Employee::getInitials)
                    .collect(Collectors.joining("-"));
            String projectLeader = project.getProjectLeader() != null ? project.getProjectLeader().getInitials() : "";

            // Pad short rows before writing to positional columns
            while (log.size() <= 16) log.add("");
            log.set(5,  projectLeader);
            log.set(6,  project.getName());
            log.set(8,  activity.getName());
            log.set(9,  project.getStartDate() != null ? project.getStartDate().toString() : "");
            log.set(10, project.getEndDate() != null ? project.getEndDate() : "");
            log.set(11, activity.getStartDate() != null ? activity.getStartDate().toString() : "");
            log.set(12, activity.getEndDate() != null ? activity.getEndDate().toString() : "");
            log.set(13, activity.getAlottedTime() != null ? activity.getAlottedTime() : "");
            log.set(14, project.getDescription() != null ? project.getDescription() : "");
            log.set(15, activity.getDescription() != null ? activity.getDescription() : "");
            log.set(16, employees);
            logs.set(i, log);
        }
        writeAll(logs);
    }
}
