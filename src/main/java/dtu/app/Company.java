package dtu.app;

import java.util.ArrayList;
import java.util.Date;

import javafx.util.converter.LocalDateStringConverter;

public class Company {
    private ArrayList<Employee> employeeList = new ArrayList<>();
    private ArrayList<Project> projectList = new ArrayList<>();
    private Employee loggedIn = null;

    public Company(){
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
    public ArrayList<Project> getProjects(){
        return this.projectList;
    }
    public ArrayList<Employee> getEmployees(){
        return this.employeeList;
    }

    
    public void setInitailsForEmployee(Employee employeeToSet, String initials) {

        assert employeeToSet != null && initials != null && initials != "";
            
        if(employeeList.indexOf(employeeToSet)== -1){                                                                               // 1
            throw new IllegalAccessError("Employee not part of company");                       
        }
        ArrayList<String> initialsList = new ArrayList<String>();
        for (Employee employee : employeeList) {
            if (employee.getInitials() != null) {
                initialsList.add(employee.getInitials());
            }

        }
        for (Employee employee : employeeList) {
            if (employeeToSet.getName().equals(employee.getName()) && initialsList.contains(initials)) {                             // 2
                throw new IllegalAccessError("Initials already exists");
            } else if (employeeToSet.getName().equals(employee.getName())) {                                                         // 3
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
            if (employee.getInitials().equals(initials)) {
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
        if(this.loggedIn == null){
            throw new IllegalAccessError("Employee not recognized");
        }
    }

    public Employee getLoggedIn() {
        return loggedIn;
    }

}
