package dtu.softwareHuset.app;
import java.time.LocalDate;

public class TimeLog {

    private static final String COMMA_DELIMITER = ",";

    Employee employee;
    Project project;
    Activity activity;
    LocalDate date;
    Double hours;
    
    public TimeLog(Employee employee, Project project, Activity activity, LocalDate date, Double hours) {
        this.employee = employee;
        this.project = project;
        this.activity = activity;
        this.date = date;
        this.hours = hours;
    }

    @Override
    public String toString() {
        return employee.getInitials() + COMMA_DELIMITER + project.getName() + COMMA_DELIMITER + activity.getName() + COMMA_DELIMITER + date.toString() + COMMA_DELIMITER + hours.toString();
    }
}
