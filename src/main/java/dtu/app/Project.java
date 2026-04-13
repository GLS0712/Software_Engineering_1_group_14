package dtu.app;

import java.util.ArrayList;

public class Project {
    private String name;
    private String time;
    private Employee projectLeader = null;
    private ArrayList<Employee> employeeList;
    private ArrayList<Activity> activityList;

    public Project(String name) {
        this.name = name;
        this.employeeList = new ArrayList<>();
        this.activityList = new ArrayList<>();
    }

    public Project(String name, String time) {
        this.name = name;
        this.time = time;
        this.employeeList = new ArrayList<>();
        this.activityList = new ArrayList<>();
    }

    public Project(String name, String time, Employee projectLeader) {
        this.name = name;
        this.time = time;
        this.projectLeader = projectLeader;
        this.employeeList = new ArrayList<>();
        this.activityList = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public String getTime() {
        return this.time;
    }

    public Employee getProjectLeader() {
        return this.projectLeader;
    }

    public void assignEmployee(Employee employee) {
        employeeList.add(employee);
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

    public void setProjectLeader(Employee employee) {
        this.projectLeader = employee;
    }
}
