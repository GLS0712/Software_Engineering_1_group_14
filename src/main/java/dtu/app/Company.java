package dtu.app;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class Company {
    private ArrayList<Employee> employeeList = new ArrayList<>();
    private ArrayList<Project> projectList = new ArrayList<>();
    private TimeLogRepository timeLogRepo = new TimeLogRepository();
    private Employee loggedIn = null;
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

    public void setInitailsForEmployee(Employee employeeToSet, String initials) {
        if(employeeList.indexOf(employeeToSet)== -1){
            throw new IllegalAccessError("Employee not part of company");
        }
        ArrayList<String> initalsList = new ArrayList<String>();
        for (Employee employee : employeeList) {
            if (employee.getInitials() != null) {
                initalsList.add(employee.getInitials());
            }

        }
        for (Employee employee : employeeList) {
            if (employeeToSet.getName().equals(employee.getName()) && initalsList.contains(employeeToSet.getInitials())) {
                throw new IllegalAccessError("Initials already exists");
            } else if (employeeToSet.getName().equals(employee.getName())) {
                employeeList.get(employeeList.indexOf(employee)).setInitails(initials);
            }
        }
       
    }

    public Employee getEmployeeFromName(String name) {
        for (Employee employee : employeeList) {
            if (employee.getName().equals(name)) {
                return employee;
            }
        }
        return null;
    }

    public void login(String initials) {
        for (Employee employee : employeeList) {
            if (employee.getInitials().equals(initials)) {
                this.loggedIn = employee;
            }
        }
        if(this.loggedIn == null || this.loggedIn.getInitials() != initials){
            throw new IllegalAccessError("Employee not recognized");
        }
    }

    public Employee getLoggedIn() {
        return loggedIn;
    }

    public void registerLog(Employee employee, Project project, Activity activity, LocalDate date, double hours) throws IOException {
        TimeLog timeLogEntry = new TimeLog(employee, project, activity, date, hours);
        timeLogRepo.registerEntry(timeLogEntry);
    }

    public void changeLog(int logNumber, List<String> log) throws IOException {
        timeLogRepo.editEntry(logNumber, log);
    }

    public List<List<String>> loadAllLogs() throws IOException{
        return timeLogRepo.load();
    }

    public List<String> getLog(int logNumber) throws IOException {
        return timeLogRepo.getEntry(logNumber);
    }
}
