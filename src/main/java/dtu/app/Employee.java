package dtu.app;

import java.util.Calendar;

public class Employee {
    private String initials;
    private String name;
    private employee_Calendar calendar;

    public Employee(String name) {
        this.name = name;
        makeInitial();
    }

    private void makeInitial() {

    }

    public String getName() {
        return this.name;
    }

    public employee_Calendar getCalendar() {
        return this.calendar;
    }

    public void setCalendar(employee_Calendar calendar) {
        this.calendar = calendar;
    }

}
