package dtu.softwareHuset.app;

import java.util.ArrayList;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class Company {
    private ArrayList<Employee> employeeList = new ArrayList<>();
    private ArrayList<Project> projectList = new ArrayList<>();
    private TimeLogRepository timeLogRepo = new TimeLogRepository();
    private Employee loggedIn = null;

    public Company() {
        this.employeeList.add(new Employee("Hubert Baumeister", "huba"));
        this.employeeList.add(new Employee("Daniel Hedegaard", "dahe"));
        this.employeeList.add(new Employee("Gustav Svare", "gusv"));
        this.employeeList.add(new Employee("Adrian Kristensen", "adkr"));
    }

    public void hireEmployee(Employee employee) {
        employeeList.add(employee);
    }

    public void createProject(String name) {
        projectList.add(new Project(name));
    }

    public void createProject(String name, String time) {
        projectList.add(new Project(name, time));
    }

    public void createProject(String name, String time, Employee employee) {
        projectList.add(new Project(name, time, employee));
    }

    public Project getProject(String name) {
        for (Project project : projectList) {
            if (project.getName().equals(name)) {
                return project;
            }
        }
        return null;
    }

    public ArrayList<Project> getProjects() {
        return this.projectList;
    }

    public ArrayList<Employee> getEmployees() {
        return this.employeeList;
    }

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

    public Employee getEmployeeFromName(String name) {
        for (Employee employee : employeeList) {
            if (employee.getName().equals(name)) {
                return employee;
            }
        }
        return null;
    }
    
    public Employee getEmployeeFromInitials(String initials) {
        for (Employee employee : this.employeeList) {
            if (initials.equals(employee.getInitials())) {
                return employee;
            }
        }
        return null;
    }

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

    public Employee getLoggedIn() {
        return loggedIn;
    }

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

    public void changeLog(int logNumber, List<String> log) throws IOException {

        timeLogRepo.editEntry(logNumber, log);
    }

    public List<List<String>> loadAllLogs() throws IOException {
        return timeLogRepo.load();
    }

    public List<String> getLog(int logNumber) throws IOException {
        return timeLogRepo.getEntry(logNumber);
    }

    public void updateLogEntry(String entryId, Employee employee, Project project, Activity activity, LocalDate date, double hours) throws IOException {
        TimeLog updatedEntry = new TimeLog(employee, project, activity, date, hours);
        
        timeLogRepo.updateEntry(entryId, updatedEntry);
    }

    public void validEmployeeInput(Employee employee) {
        if (getEmployeeFromName(employee.getName()) == null) {
            throw new IllegalArgumentException("Employee not found");
        }
    }

    public void validProjectInput(Project project) {
        if (getProject(project.getName()) == null) {
            throw new IllegalArgumentException("Project not found");
        }
    }

    public void validActivityInput(Activity activity, Project project) {
        if (project.getActivityFromName(activity.getName()) == null) {
            throw new IllegalArgumentException("Activity not found");
        }
    }

    public void validDateInput(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot log time on a future date");
        }
    }

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
