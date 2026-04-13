package dtu.app;

import java.util.ArrayList;
import java.util.Calendar;

public class Company {
    private ArrayList<Employee> employeeList = new ArrayList<>();
    private ArrayList<Project> projectList = new ArrayList<>();

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

}
