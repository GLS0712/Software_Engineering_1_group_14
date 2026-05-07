package dtu.app;

public class Employee {
    private String initials;
    private String name;
    private Employee_Calendar calendar;

    public Employee(String name) {
        this.name = name;
    }
    public Employee(String name, String initials) {
        this.name = name;
        this.initials = initials;
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
      
    public String getInitials() {
        return this.initials;
    }

    public void setInitails(String initials) {
        this.initials = initials;
    }
}
