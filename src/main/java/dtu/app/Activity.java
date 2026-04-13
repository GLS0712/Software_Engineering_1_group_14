package dtu.app;

public class Activity {
    private String name;
    private String description;

    public Activity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}
