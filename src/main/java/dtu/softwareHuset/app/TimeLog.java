package dtu.softwareHuset.app;
import java.time.LocalDate;

// Represents a single time log entry that maps to one CSV row.
// Column order (0-indexed, matching TimeLogRepository):
//   0: entryId (prepended by TimeLogRepository)
//   1: employeeId (initials)
//   2: projectId
//   3: activityName
//   4: date
//   5: hours
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

    // Serializes this entry to a comma-separated string covering columns 1-5.
    // Column 0 (entryId) is prepended by TimeLogRepository when writing to the file.
    @Override
    // Author: GubbeMK
    public String toString() {
        return employee.getInitials() + COMMA_DELIMITER     // col 1: employeeId
                + project.getId() + COMMA_DELIMITER         // col 2: projectId
                + activity.getName() + COMMA_DELIMITER      // col 3: activityName
                + date.toString() + COMMA_DELIMITER         // col 4: date
                + hours.toString();                         // col 5: hours
    }
}
