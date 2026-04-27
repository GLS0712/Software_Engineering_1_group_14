package dtu.app;

import java.util.Calendar;

public class Employee {
    private String initials;
    private String name;
    private Employee_Calendar calendar;

    public Employee(String name) {
        this.name = name;
        makeInitial();
    }

    private void makeInitial() {

    }

    public String getName() {
        return this.name;
    }

    public Employee_Calendar getCalendar() {
        return this.calendar;
    }

    public void setCalendar(Employee_Calendar calendar) {
        this.calendar = calendar;
    }

}
