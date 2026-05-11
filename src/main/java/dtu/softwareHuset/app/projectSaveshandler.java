package dtu.softwareHuset.app;

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
import java.util.stream.Collectors;

// Handles all CSV read/write operations for projects and activities in projectSaves.csv.
// Column layout (0-indexed):
//   0  projectId         1  projectLeader     2  projectName
//   3  activityId        4  activityName      5  projectStartDate
//   6  projectEndDate    7  activityStartDate  8  activityEndDate
//   9  Alloted time      10 projectDescription 11 activityDescription
//  12  employees (dash-separated initials)
public class projectSaveshandler {

    private static final String COMMA_DELIMITER = ",";
    private static final File file = new File("data/projectSaves.csv");
    private static final String HEADER = "projectId,projectLeader,projectName,activityId,activityName,projectStartDate,projectEndDate,activityStartDate,activityEndDate,Alloted time,projectDescription,activityDescription,employees";

    // Reads all non-header, non-blank rows from the CSV.
    // Author: GedeGustav
    public List<List<String>> load() throws IOException {
        List<List<String>> rows = new ArrayList<>();
        if (!file.exists()) return rows;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; }
                if (line.isBlank()) continue;
                // -1 limit keeps trailing empty fields that split() would otherwise drop
                String[] entries = line.split(COMMA_DELIMITER, -1);
                rows.add(new ArrayList<>(Arrays.asList(entries)));
            }
        }
        return rows;
    }

    // Sorts rows by projectId then activityId and writes them with the standard header.
    // Author: GedeGustav
    public void writeAll(List<List<String>> rows) throws IOException {
        rows.sort((a, b) -> {
            String pid1 = !a.isEmpty() ? a.get(0) : "";
            String pid2 = !b.isEmpty() ? b.get(0) : "";
            int cmp = pid1.compareTo(pid2);
            if (cmp != 0) return cmp;
            String aid1 = a.size() > 3 ? a.get(3) : "";
            String aid2 = b.size() > 3 ? b.get(3) : "";
            return aid1.compareTo(aid2);
        });
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(HEADER);
            bw.newLine();
            for (List<String> row : rows) {
                bw.write(String.join(COMMA_DELIMITER, row));
                bw.newLine();
            }
        }
    }

    // Saves a project and optional activity to the CSV.
    // When activity is null, writes a project-only row.
    // When adding the first activity to a project, reuses the existing project-only row.
    // Author: GedeGustav
    public void writeEntry(Project project, Activity activity) throws IOException {
        List<List<String>> existing = load();
        file.getParentFile().mkdirs();

        String projectLeader       = project.getProjectLeader() != null ? project.getProjectLeader().getInitials() : "";
        String projectStartDate    = project.getStartDate() != null ? project.getStartDate().toString() : "";
        String projectEndDate      = project.getEndDate() != null ? project.getEndDate() : "";
        String projectDescription  = project.getDescription() != null ? project.getDescription() : "";
        String activityId          = activity != null ? activity.getId() : "";
        String activityName        = activity != null ? activity.getName() : "";
        String activityStartDate   = (activity != null && activity.getStartDate() != null) ? activity.getStartDate().toString() : "";
        String activityEndDate     = (activity != null && activity.getEndDate() != null) ? activity.getEndDate().toString() : "";
        String allottedTime        = (activity != null && activity.getAlottedTime() != null) ? activity.getAlottedTime() : "";
        String activityDescription = (activity != null && activity.getDescription() != null) ? activity.getDescription() : "";

        // When adding the first activity to a project, reuse the project-only row
        // (identified by a matching projectId with an empty activityId)
        if (activity != null) {
            for (int i = 0; i < existing.size(); i++) {
                List<String> row = existing.get(i);
                if (!row.isEmpty() && row.get(0).equals(project.getId())
                        && (row.size() <= 3 || row.get(3).isEmpty())) {
                    List<String> updated = new ArrayList<>(row);
                    while (updated.size() <= 12) updated.add("");
                    updated.set(3,  activityId);
                    updated.set(4,  activityName);
                    updated.set(7,  activityStartDate);
                    updated.set(8,  activityEndDate);
                    updated.set(9,  allottedTime);
                    updated.set(11, activityDescription);
                    existing.set(i, updated);
                    writeAll(existing);
                    return;
                }
            }
        }

        // No matching row found — append a new one
        List<String> row = new ArrayList<>(Arrays.asList(
            project.getId(),
            projectLeader,
            project.getName(),
            activityId,
            activityName,
            projectStartDate,
            projectEndDate,
            activityStartDate,
            activityEndDate,
            allottedTime,
            projectDescription,
            activityDescription,
            ""  // employees
        ));
        existing.add(row);
        writeAll(existing);
    }

    // Reconstructs in-memory Project and Activity objects from the CSV.
    // Projects and activities that already exist in the provided lists are skipped
    // to avoid duplicates on repeated calls.
    // Author: GedeGustav
    public void loadProjects(List<Project> projectList, List<Employee> employeeList) throws IOException {
        List<List<String>> rows = load();
        for (List<String> row : rows) {
            String projectId           = row.get(0);
            String projectLeaderInit   = row.size() > 1  ? row.get(1)  : "";
            String projectName         = row.size() > 2  ? row.get(2)  : "";
            String activityId          = row.size() > 3  ? row.get(3)  : "";
            String activityName        = row.size() > 4  ? row.get(4)  : "";
            String projectStartDate    = row.size() > 5  ? row.get(5)  : "";
            String projectEndDate      = row.size() > 6  ? row.get(6)  : "";
            String activityStartDate   = row.size() > 7  ? row.get(7)  : "";
            String activityEndDate     = row.size() > 8  ? row.get(8)  : "";
            String allottedTime        = row.size() > 9  ? row.get(9)  : "";
            String projectDescription  = row.size() > 10 ? row.get(10) : "";
            String activityDescription = row.size() > 11 ? row.get(11) : "";
            String employeesStr        = row.size() > 12 ? row.get(12) : "";

            Project project = projectList.stream()
                    .filter(p -> p.getName().equals(projectName))
                    .findFirst().orElse(null);
            if (project == null) {
                project = new Project(projectName);
                project.setId(projectId);
                projectList.add(project);
            }

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

            if (!activityName.isEmpty() && project.getActivityFromName(activityName) == null) {
                Activity a = new Activity(activityName, activityDescription);
                a.setId(activityId);
                if (!activityStartDate.isEmpty()) a.setStartDate(LocalDate.parse(activityStartDate));
                if (!activityEndDate.isEmpty())   a.setEndDate(LocalDate.parse(activityEndDate));
                if (!allottedTime.isEmpty())      a.setAlottedTime(allottedTime);
                project.getActivities().add(a);
            }

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

    // Pushes the current in-memory state of all projects and activities back into the CSV.
    // Only rows matched to a known project and activity are updated; unmatched rows are left unchanged.
    // Author: GedeGustav
    public void sync(List<Project> projects) throws IOException {
        List<List<String>> rows = load();
        for (int i = 0; i < rows.size(); i++) {
            List<String> row = new ArrayList<>(rows.get(i));

            Project project = projects.stream()
                    .filter(p -> p.getId().equals(row.get(0)))
                    .findFirst().orElse(null);
            if (project == null) continue;

            Activity activity = project.getActivityById(row.size() > 3 ? row.get(3) : "");
            if (activity == null) continue;

            String employees = activity.getEmployees().stream()
                    .map(Employee::getInitials)
                    .collect(Collectors.joining("-"));
            String projectLeader = project.getProjectLeader() != null ? project.getProjectLeader().getInitials() : "";

            while (row.size() <= 12) row.add("");
            row.set(1,  projectLeader);
            row.set(2,  project.getName());
            row.set(4,  activity.getName());
            row.set(5,  project.getStartDate() != null ? project.getStartDate().toString() : "");
            row.set(6,  project.getEndDate() != null ? project.getEndDate() : "");
            row.set(7,  activity.getStartDate() != null ? activity.getStartDate().toString() : "");
            row.set(8,  activity.getEndDate() != null ? activity.getEndDate().toString() : "");
            row.set(9,  activity.getAlottedTime() != null ? activity.getAlottedTime() : "");
            row.set(10, project.getDescription() != null ? project.getDescription() : "");
            row.set(11, activity.getDescription() != null ? activity.getDescription() : "");
            row.set(12, employees);
            rows.set(i, row);
        }
        writeAll(rows);
    }

    // Removes all rows belonging to the given project.
    // Author: GedeGustav
    public void deleteProject(String projectId) throws IOException {
        List<List<String>> rows = load();
        rows.removeIf(row -> !row.isEmpty() && row.get(0).equals(projectId));
        writeAll(rows);
    }

    // Removes all rows belonging to the given activity.
    // Author: GedeGustav
    public void deleteActivity(String activityId) throws IOException {
        List<List<String>> rows = load();
        rows.removeIf(row -> row.size() > 3 && row.get(3).equals(activityId));
        writeAll(rows);
    }
}
