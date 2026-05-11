package dtu.softwareHuset.app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Activity {
    private String name;
    private String description;
    private String AlottedTime;
    private String id = "00000";
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Employee> employees = new ArrayList<>();
    // Sentinel used in place of a null end date when calculating calendar availability
    private static final LocalDate INDEFINITE = LocalDate.of(2099, 12, 31);

    // Constructor used when creating an activity without explicit dates — start defaults to today
    // Author: GLS0712
    public Activity(String name, String description) {
        this.name = name;
        this.description = description;
        this.startDate = LocalDate.now();
    }

    // Constructor used when both start and end dates are known upfront
    // Author: GLS0712
    public Activity(String name, String description, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Author: GLS0712
    public String getId() {
        return this.id;
    }

    // Generates an ID in the format "Ayy###" (e.g. "A25001") based on the current year and activity count
    // Author: GLS0712
    public void setId(int activityCount) {
        DateFormat df = new SimpleDateFormat("yy");
        id = "A" + df.format(Calendar.getInstance().getTime()) + String.format("%03d", activityCount);
    }

    // Author: GLS0712
    public void setId(String id) {
        this.id = id;
    }

    // Author: GLS0712
    public String getName() {
        return this.name;
    }
    // Author: Daniel Hedegaard
    public String getAlottedTime(){
        return this.AlottedTime;
    }
    // Author: GLS0712
    public String getDescription() {
        return this.description;
    }

    // Author: GLS0712
    public LocalDate getStartDate() {
        return this.startDate;
    }

    // Author: GLS0712
    public LocalDate getEndDate() {
        return this.endDate;
    }

    // Returns INDEFINITE when no end date is set so calendar range checks still work
    // Author: GLS0712
    private LocalDate effectiveEnd() {
        return endDate != null ? endDate : INDEFINITE;
    }

    // Adds an employee to the activity after checking two availability constraints:
    // 1. Hard block (IllegalStateException): employee has time off or sick leave in the period — cannot override
    // 2. Soft warning (IllegalArgumentException): employee already has 10 activities on some day — UI can still force-add
    // Author: GLS0712
    public void addEmployee(Employee employee) {
        if (startDate != null && employee.getCalendar().hasTimeOffInPeriod(startDate, effectiveEnd())) {
            throw new IllegalStateException("Cannot add employee: they have sick leave or time off during this period");
        }
        if (startDate != null && !employee.getCalendar().isAvailableForPeriod(startDate, effectiveEnd())) {
            throw new IllegalArgumentException("are you sure this activity should be added, schedule is full");
        }
        employees.add(employee);
        // Register the activity on the employee's calendar so availability checks work going forward
        if (startDate != null) {
            employee.getCalendar().registerActivity(startDate, effectiveEnd(), name);
        }
    }

    // Force-adds an employee even if their schedule is full, but still blocks on time off/sick leave.
    // Called after the user confirms the soft-warning prompt in the UI.
    // Author: GLS0712
    public void forceAddEmployee(Employee employee) {
        if (startDate != null && employee.getCalendar().hasTimeOffInPeriod(startDate, effectiveEnd())) {
            throw new IllegalStateException("Cannot add employee: they have sick leave or time off during this period");
        }
        employees.add(employee);
        if (startDate != null) {
            employee.getCalendar().forceRegisterActivity(startDate, effectiveEnd(), name);
        }
    }

    // Removes an employee from the activity and cleans up their calendar entry
    // Author: GLS0712
    public void removeEmployee(Employee employeeToRemove) {
        Employee employeeToBeRemoved = null;
        for (Employee employeeInActivity : employees) {
            if (employeeInActivity.equals(employeeToRemove)) {
                employeeToBeRemoved = employeeInActivity;
            }
        }
        if (employeeToBeRemoved != null) {
            this.employees.remove(employeeToBeRemoved);
            // Remove the calendar entry so the employee's availability is freed up
            if (startDate != null) {
                employeeToBeRemoved.getCalendar().removeActivity(startDate, effectiveEnd(), name);
            }
        }
    }

    // Author: Daniel Hedegaard
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    // Author: GLS0712
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    // Author: Daniel Hedegaard
    public void setAlottedTime(String AlottedTime){
        this.AlottedTime = AlottedTime;
    }

    // Author: GLS0712
    public List<Employee> getEmployees() {
        return employees;
    }

    // Author: Daniel Hedegaard
    public void setName(String name) {
        this.name = name;
    }

    // Author: Daniel Hedegaard
    public void setDescription(String description) {
        this.description = description;
    }

    // Called after editing an activity's dates or name — removes the old calendar entries for all
    // assigned employees and re-registers them under the new dates/name so calendars stay in sync
    // Author: GLS0712
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
