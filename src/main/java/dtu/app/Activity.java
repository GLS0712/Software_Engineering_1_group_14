package dtu.app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Activity {
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Employee> employees = new ArrayList<>();

    public Activity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Activity(String name, String description, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}
