package dtu.softwareHuset.app;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import javafx.util.converter.LocalDateStringConverter;

public class Company {
    private ArrayList<Employee> employeeList = new ArrayList<>();
    private ArrayList<Project> projectList = new ArrayList<>();
    private TimeLogRepository timeLogRepo = new TimeLogRepository();
    private projectSaveshandler projectSavesRepo = new projectSaveshandler();
    private Employee loggedIn = null;

    // Author: GubbeMK
    public Company() {
        this.employeeList.add(new Employee("Hubert Baumeister", "huba"));
        this.employeeList.add(new Employee("Daniel Hedegaard", "dahe"));
        this.employeeList.add(new Employee("Gustav Svare", "gusv"));
        this.employeeList.add(new Employee("Adrian Kristensen", "adkr"));
    }

    // Author: GLS0712
    public void hireEmployee(Employee employee) {
        employeeList.add(employee);
    }

    // Author: GLS0712
    public void createProject(String name) {
        projectList.add(new Project(name));
    }

    // Author: GLS0712
    public void createProject(String name, String time) {
        projectList.add(new Project(name, time));
    }

    // Author: GLS0712
    public void createProject(String name, String time, Employee employee) {
        projectList.add(new Project(name, time, employee));
    }

    // Author: GLS0712
    public Project getProject(String name) {
        for (Project project : projectList) {
            if (project.getName().equals(name)) {
                return project;
            }
        }
        return null;
    }

    // Author: GLS0712
    public Project getProjectById(String id) {
        for (Project project : projectList) {
            if (id.equals(project.getId())) {
                return project;
            }
        }
        return null;
    }

    // Author: GubbeMK
    public ArrayList<Project> getProjects() {
        return this.projectList;
    }

    // Author: GubbeMK
    public ArrayList<Employee> getEmployees() {
        return this.employeeList;
    }

    // Author: Daniel Hedegaard
    public void setInitailsForEmployee(Employee employeeToSet, String initials) {

        assert employeeToSet != null && initials != null && initials != "";

        if (employeeList.indexOf(employeeToSet) == -1) { // 1
            throw new IllegalAccessError("Employee not part of company");
        }
        ArrayList<String> initialsList = new ArrayList<String>();
        for (Employee employee : employeeList) {
            if (employee.getInitials() != null) {
                initialsList.add(employee.getInitials());
            }

        }
        for (Employee employee : employeeList) {
            if (employeeToSet.getName().equals(employee.getName()) && initialsList.contains(initials)) { // 2
                throw new IllegalAccessError("Initials already exists");
            } else if (employeeToSet.getName().equals(employee.getName())) { // 3
                employeeList.get(employeeList.indexOf(employee)).setInitails(initials);
            }
        }

        assert employeeToSet.getInitials() == initials;
    }

    // Author: Daniel Hedegaard
    public Employee getEmployeeFromName(String name) {
        for (Employee employee : employeeList) {
            if (employee.getName().equals(name)) {
                return employee;
            }
        }
        return null;
    }

    // Author: GLS0712
    public Employee getEmployeeFromInitials(String initials) {
        for (Employee employee : this.employeeList) {
            if (initials.equals(employee.getInitials())) {
                return employee;
            }
        }
        return null;
    }

    // Author: Daniel Hedegaard
    public void login(String initials) {
        for (Employee employee : employeeList) {
            if (initials.equals(employee.getInitials())) {
                this.loggedIn = employee;
            }
        }
        if (this.loggedIn == null) {
            throw new IllegalAccessError("Employee not recognized");
        }
    }

    // Author: Daniel Hedegaard
    public Employee getLoggedIn() {
        return loggedIn;
    }

    // Author: GubbeMK
    public void registerLog(Employee employee, Project project, Activity activity, LocalDate date, double hours)
            throws IOException {

        validEmployeeInput(employee);
        validProjectInput(project);
        validActivityInput(activity, project);
        validDateInput(date);
        validHourInput(hours);

        TimeLog timeLogEntry = new TimeLog(employee, project, activity, date, hours);
        timeLogRepo.registerEntry(timeLogEntry);
    }

    // Author: GubbeMK
    public void changeLog(int logNumber, List<String> log) throws IOException {

        timeLogRepo.editEntry(logNumber, log);
    }

    // Author: GubbeMK
    public List<List<String>> loadAllLogs() throws IOException {
        return timeLogRepo.load();
    }

    // Author: GubbeMK
    public List<String> getLog(int logNumber) throws IOException {
        return timeLogRepo.getEntry(logNumber);
    }

    // Author: GLS0712
    public void loadProjectsFromLogs() throws IOException {
        projectSavesRepo.loadProjects(projectList, employeeList);
    }

    // Author: GLS0712
    public void syncLogs() throws IOException {
        projectSavesRepo.sync(projectList);
    }

    // Author: GLS0712
    public void writeProjectStub(Project project) throws IOException {
        projectSavesRepo.writeEntry(project, null);
    }

    // Author: GLS0712
    public void writeActivityStub(Project project, Activity activity) throws IOException {
        projectSavesRepo.writeEntry(project, activity);
    }

    // Author: GLS0712
    public void deleteProject(Project project) throws IOException {
        projectSavesRepo.deleteProject(project.getId());
        timeLogRepo.deleteEntriesForProject(project.getId());
        projectList.remove(project);
    }

    // Author: GLS0712
    public void deleteActivity(Project project, Activity activity) throws IOException {
        projectSavesRepo.deleteActivity(activity.getId());
        timeLogRepo.deleteEntriesForActivity(activity.getName());
        project.getActivities().remove(activity);
    }

    // Author: GLS0712
    public void deleteLogEntry(String entryId) throws IOException {
        timeLogRepo.deleteEntry(entryId);
    }

    // Author: GubbeMK
    public void updateLogEntry(String entryId, Employee employee, Project project, Activity activity, LocalDate date,
            double hours) throws IOException {
        TimeLog updatedEntry = new TimeLog(employee, project, activity, date, hours);

        timeLogRepo.updateEntry(entryId, updatedEntry);
    }

    // Author: GubbeMK
    public void validEmployeeInput(Employee employee) {
        if (getEmployeeFromName(employee.getName()) == null) {
            throw new IllegalArgumentException("Employee not found");
        }
    }

    // Author: GubbeMK
    public void validProjectInput(Project project) {
        if (getProject(project.getName()) == null) {
            throw new IllegalArgumentException("Project not found");
        }
    }

    // Author: GubbeMK
    public void validActivityInput(Activity activity, Project project) {
        if (project.getActivityFromName(activity.getName()) == null) {
            throw new IllegalArgumentException("Activity not found");
        }
    }

    // Author: GubbeMK
    public void validDateInput(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot log time on a future date");
        }
        // Postcondition: date is valid for logging
        assert date != null && !date.isAfter(LocalDate.now()) : "Postcondition violated";
    }

    // Author: GubbeMK
    public void validHourInput(double hours) {
        if (hours <= 0) {
            throw new IllegalArgumentException("Hours must be positive");
        }
        if (hours > 24) {
            throw new IllegalArgumentException("Hours cannot exceed 24");
        }
        if (hours % 0.5 != 0) {
            throw new IllegalArgumentException("Hours must be in half-hour increments");
        }
    }
}
