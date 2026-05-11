package dtu.softwareHuset.app;

public class Employee {
    private String initials;
    private String name;
    private Employee_Calendar calendar;

    // Author: GLS0712
    public Employee(String name) {
        this.name = name;
        this.calendar = new Employee_Calendar(name);
    }
    // Author: Daniel Hedegaard
    public Employee(String name, String initials) {
        this.name = name;
        this.initials = initials;
        this.calendar = new Employee_Calendar(name);
    }
  
    // Author: GLS0712
    public String getName() {
        return this.name;
    }

    // Author: GLS0712
    public Employee_Calendar getCalendar() {
        return this.calendar;
    }

    // Author: GLS0712
    public void setCalendar(Employee_Calendar calendar) {
        this.calendar = calendar;
    }
      
    // Author: Daniel Hedegaard
    public String getInitials() {
        return this.initials;
    }

    // Author: Daniel Hedegaard
    public void setInitails(String initials) {
        this.initials = initials;
    }
}
