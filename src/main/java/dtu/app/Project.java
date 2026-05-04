package dtu.app;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Project {
    private String name;
    private String endDate; // yyyy-mm-dd
    private String description;
    private Employee projectLeader = null;
    private ArrayList<Employee> employeeList;
    private ArrayList<Activity> activityList;

    public Project(String name) {
        this.name = name;
        this.employeeList = new ArrayList<>();
        this.activityList = new ArrayList<>();
    }

    public Project(String name, String endDate) {
        this.name = name;
        this.endDate = endDate;
        this.employeeList = new ArrayList<>();
        this.activityList = new ArrayList<>();
    }

    public Project(String name, String endDate, Employee projectLeader) {
        this.name = name;
        this.endDate = endDate;
        this.projectLeader = projectLeader;
        this.employeeList = new ArrayList<>();
        this.activityList = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }
    public String getDescription(){
        return this.description;
    }
    public String getEndDate() {
        return this.endDate;
    }

    public Employee getProjectLeader() {
        return this.projectLeader;
    }

    public void assignEmployee(Employee employee) {
        employeeList.add(employee);
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void createActivity(Employee employee, String name, String description) throws IllegalAccessError {
        if (projectLeader == null || this.projectLeader.getName().equals(employee.getName())) {
            this.activityList.add(new Activity(name, description));
        } else {
            throw new IllegalAccessError("you are not projectLeader");
        }
    }

    public Activity getActivityFromName(String name) {
        for (Activity activity : activityList) {
            if (activity.getName().equals(name)) {
                return activity;
            }
        }

        return null;
    }

    public void createActivity(Employee employee, String name, String description, LocalDate startDate, LocalDate endDate) throws IllegalAccessError {
        if (projectLeader == null || this.projectLeader.getName().equals(employee.getName())) {
            LocalDate effectiveEnd = capToProjectEndDate(endDate);
            this.activityList.add(new Activity(name, description, startDate, effectiveEnd));
        } else {
            throw new IllegalAccessError("you are not projectLeader");
        }
    }

    public void changeActivityEndDate(Employee employee, String activityName, LocalDate newEndDate) throws IllegalAccessError {
        if (projectLeader != null && !this.projectLeader.getName().equals(employee.getName())) {
            throw new IllegalAccessError("you are not projectLeader");
        }
        getActivityFromName(activityName).setEndDate(capToProjectEndDate(newEndDate));
    }

    private LocalDate capToProjectEndDate(LocalDate date) {
        if (this.endDate != null && !this.endDate.isEmpty()) {
            LocalDate projectEnd = LocalDate.parse(this.endDate);
            if (date.isAfter(projectEnd)) {
                return projectEnd;
            }
        }
        return date;
    }

    public void addEmployeeToActivity(Employee requester, Employee employeeToAdd, String activityName) {
        if (projectLeader != null && !projectLeader.getName().equals(requester.getName())) {
            throw new IllegalAccessError("you are not projectLeader");
        }
        Activity activity = getActivityFromName(activityName);
        Employee_Calendar cal = employeeToAdd.getCalendar();
        if (cal != null && activity.getStartDate() != null && activity.getEndDate() != null) {
            int days = (int) activity.getStartDate().until(activity.getEndDate(), ChronoUnit.DAYS);
            for (int i = 0; i < days; i++) {
                LocalDate day = activity.getStartDate().plusDays(i);
                if (cal.getEntries(day).size() >= 10) {
                    throw new IllegalArgumentException("Employee is not available during the activity period");
                }
            }
        }
        activity.addEmployee(employeeToAdd);
    }

    public void setProjectLeader(Employee employee) {
        this.projectLeader = employee;
    }

    public void setEndDate(String newEndDate) {
        this.endDate = newEndDate;
        LocalDate newEnd = LocalDate.parse(newEndDate);
        for (Activity activity : activityList) {
            if (activity.getEndDate() != null && activity.getEndDate().isAfter(newEnd)) {
                activity.setEndDate(newEnd);
            }
        }
    }
}
