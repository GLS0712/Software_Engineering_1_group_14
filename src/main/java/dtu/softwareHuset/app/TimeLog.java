package dtu.softwareHuset.app;
import java.time.LocalDate;
import java.util.stream.Collectors;

// Represents a single time log entry that maps to one CSV row.
// Column order (0-indexed, matching TimeLogRepository):
//   0: entryId (prepended by TimeLogRepository)
//   1: employeeId (initials)
//   2: employeeLoggedTime (hours)
//   3: loggedTimeDate
//   4: projectId
//   5: projectLeader (initials)
//   6: projectName
//   7: activityId
//   8: activityName
//   9: projectStartDate
//  10: projectEndDate
//  11: activityStartDate
//  12: activityEndDate
//  13: allottedTime
//  14: projectDescription
//  15: activityDescription
//  16: employees (dash-separated initials)
public class TimeLog {

    private static final String COMMA_DELIMITER = ",";

    Employee employee;
    Project project;
    Activity activity;
    LocalDate date;
    Double hours;

    // Constructs a log entry for the given employee, project, activity, date and hours.
    // Author: GubbeMK
    public TimeLog(Employee employee, Project project, Activity activity, LocalDate date, Double hours) {
        this.employee = employee;
        this.project = project;
        this.activity = activity;
        this.date = date;
        this.hours = hours;
    }

    // Serializes this entry to a comma-separated string covering columns 1-16.
    // Column 0 (entryId) is prepended by TimeLogRepository when writing to the file.
    // Null fields are written as empty strings to keep the column count stable.
    @Override
    // Author: GubbeMK
    public String toString() {
        // Employees assigned to the activity are stored as dash-separated initials (col 16)
        String employees = activity.getEmployees().stream()
                .map(Employee::getInitials)
                .collect(Collectors.joining("-"));
        String projectLeader = project.getProjectLeader() != null ? project.getProjectLeader().getInitials() : "";
        String projectStartDate = project.getStartDate() != null ? project.getStartDate().toString() : "";
        String projectEndDate = project.getEndDate() != null ? project.getEndDate() : "";
        String activityStartDate = activity.getStartDate() != null ? activity.getStartDate().toString() : "";
        String activityEndDate = activity.getEndDate() != null ? activity.getEndDate().toString() : "";
        String allottedTime = activity.getAlottedTime() != null ? activity.getAlottedTime() : "";
        return employee.getInitials() + COMMA_DELIMITER     // col 1: employeeId
                + hours.toString() + COMMA_DELIMITER        // col 2: employeeLoggedTime
                + date.toString() + COMMA_DELIMITER         // col 3: loggedTimeDate
                + project.getId() + COMMA_DELIMITER         // col 4: projectId
                + projectLeader + COMMA_DELIMITER           // col 5: projectLeader
                + project.getName() + COMMA_DELIMITER       // col 6: projectName
                + activity.getId() + COMMA_DELIMITER        // col 7: activityId
                + activity.getName() + COMMA_DELIMITER      // col 8: activityName
                + projectStartDate + COMMA_DELIMITER        // col 9: projectStartDate
                + projectEndDate + COMMA_DELIMITER          // col 10: projectEndDate
                + activityStartDate + COMMA_DELIMITER       // col 11: activityStartDate
                + activityEndDate + COMMA_DELIMITER         // col 12: activityEndDate
                + allottedTime + COMMA_DELIMITER            // col 13: allottedTime
                + (project.getDescription() != null ? project.getDescription() : "") + COMMA_DELIMITER // col 14: projectDescription
                + (activity.getDescription() != null ? activity.getDescription() : "") + COMMA_DELIMITER // col 15: activityDescription
                + employees;                                // col 16: employees
    }
}
