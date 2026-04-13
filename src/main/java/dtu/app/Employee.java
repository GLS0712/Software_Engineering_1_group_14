package dtu.app;

public class Employee {
    private String initials;
    private String name;

    public Employee(String name) {
        this.name = name;
        makeInitial();
    }

    private void makeInitial() {

    }

    public String getName() {
        return this.name;
    }

}
