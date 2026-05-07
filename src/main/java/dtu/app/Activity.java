package dtu.app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Activity {
    private String name;
    private String description;
    private String AlottedTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Employee> employees = new ArrayList<>();
    private static final LocalDate INDEFINITE = LocalDate.of(2099, 12, 31);

    public Activity(String name, String description) {
        this.name = name;
        this.description = description;
        this.startDate = LocalDate.now();
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
    public String getAlottedTime(){
        return this.AlottedTime;
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

    private LocalDate effectiveEnd() {
        return endDate != null ? endDate : INDEFINITE;
    }

    public void addEmployee(Employee employee) {
        if (startDate != null && !employee.getCalendar().isAvailableForPeriod(startDate, effectiveEnd())) {
            throw new IllegalArgumentException("are you sure this activity should be added, schedule is full");
        }
        employees.add(employee);
        if (startDate != null) {
            employee.getCalendar().registerActivity(startDate, effectiveEnd(), name);
        }
    }

    public void forceAddEmployee(Employee employee) {
        employees.add(employee);
        if (startDate != null) {
            employee.getCalendar().forceRegisterActivity(startDate, effectiveEnd(), name);
        }
    }

    public void removeEmployee(Employee employeeToRemove) {
        Employee employeeToBeRemoved = null;
        for (Employee employeeInActivity : employees) {
            if (employeeInActivity.equals(employeeToRemove)) {
                employeeToBeRemoved = employeeInActivity;
            }
        }
        if (employeeToBeRemoved != null) {
            this.employees.remove(employeeToBeRemoved);
            if (startDate != null) {
                employeeToBeRemoved.getCalendar().removeActivity(startDate, effectiveEnd(), name);
            }
        }
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public void setAlottedTime(String AlottedTime){
        this.AlottedTime = AlottedTime;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void updateEmployeeCalendars(LocalDate oldStartDate, LocalDate oldEndDate, String oldName) {
        for (Employee employee : employees) {
            if (oldStartDate != null) {
                LocalDate effectiveOldEnd = oldEndDate != null ? oldEndDate : INDEFINITE;
                employee.getCalendar().removeActivity(oldStartDate, effectiveOldEnd, oldName);
            }
            if (startDate != null) {
                employee.getCalendar().registerActivity(startDate, effectiveEnd(), name);
            }
        }
    }
}
