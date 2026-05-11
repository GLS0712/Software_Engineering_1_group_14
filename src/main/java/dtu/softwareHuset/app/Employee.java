package dtu.softwareHuset.app;

public class Employee {
    private String initials;
    private String name;
    private Employee_Calendar calendar;

    // Author: GedeGustav
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
  
    // Author: GedeGustav
    public String getName() {
        return this.name;
    }

    // Author: GedeGustav
    public Employee_Calendar getCalendar() {
        return this.calendar;
    }

    // Author: GedeGustav
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
