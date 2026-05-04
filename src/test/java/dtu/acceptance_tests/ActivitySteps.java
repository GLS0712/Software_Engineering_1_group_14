package dtu.acceptance_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dtu.app.Activity;
import dtu.app.Company;
import dtu.app.Employee;
import dtu.app.Employee_Calendar;
import dtu.app.Project;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ActivitySteps {

    Company company;
    Project project;
    Activity activity;
    Employee employee;
    Map<String, Employee> employeeMap = new HashMap<>();
    ErrorMessageHandler errorMessageHandler;

    public ActivitySteps(Company company, ErrorMessageHandler errorMessageHandler) {
        this.company = company;
        this.errorMessageHandler = errorMessageHandler;
    }

    @Given("there is a project")
    public void thereIsAProject() {
        company.createProject("name");
        project = company.getProject("name");
    }

    @Then("there exists an activity with {string} and {string}")
    public void thereExistsAnActivityWithAnd(String string, String string2) {
        assertNotNull(project.getActivityFromName(string));
        assertEquals(string2, project.getActivityFromName(string).getDescription());
    }

    @Given("there is no projectleader")
    public void there_is_no_projectleader() {
        project.setProjectLeader(null);
    }

    @Given("an employee {string} is assigned to project")
    public void an_employee_is_assigned_to_project(String s) {
        employee = new Employee(s);
        employeeMap.put(s, employee);
        project.assignEmployee(employee);
    }

    @When("{string} creates activity with {string} and {string}")
    public void John_doe_creates_activity_with_and(String s, String s2, String s3) {
        try {
            project.createActivity(employee, s2, s3);
        } catch (IllegalAccessError e) {
            errorMessageHandler.setErrorMessage(e.getMessage());
        }
    }

    @Given("the projectLeader is {string}")
    public void the_projectLeader_is_John_doe(String s) {
        project.setProjectLeader(employee);
    }

    @Then("the error message is {string}")
    public void the_error_message_is(String s) {
        assertEquals(s, errorMessageHandler.getErrorMessage());
    }

    @Given("the projectLeader is not {string}")
    public void the_projectLeader_is_not(String s) {
        project.setProjectLeader(new Employee("bingus"));
    }

    @Then("the activity {string} is found")
    public void the_activity_is_found(String s) {
        assertEquals(s, activity.getName());
    }

    @When("an employee {string} searches for the activity {string}")
    public void an_employee_searches_for_the_activity(String s, String s2) {
        activity = project.getActivityFromName(s2);
    }

    @Given("there is an activity with name {string}")
    public void there_is_an_activity_with_name(String s) {
        project.createActivity(employee, s, "Who cares");
    }

    @Then("the activity {string} is Not found")
    public void the_activity_is_Not_found(String s) {
        assertNull(activity);
    }

    @Given("there is not an activity with name {string}")
    public void there_is_not_an_activity_with_name(String s) {
        project.createActivity(employee, "ahhh", "Ben");
    }

    @Given("{string} has a personal calendar")
    public void has_a_personal_calendar(String name) {
        Employee emloyee = employeeMap.getOrDefault(name, employee);
        emloyee.setCalendar(new Employee_Calendar(name));
    }

    // Uses the project leader as creator when one exists, so two-employee scenarios work correctly
    @Given("there is an activity {string} from {string} to {string}")
    public void there_is_an_activity_from_to(String activityName, String startDate, String endDate) {
        Employee creator = project.getProjectLeader() != null ? project.getProjectLeader() : employee;
        project.createActivity(creator, activityName, "description", LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @Given("{string} has a full schedule on {string}")
    public void has_a_full_schedule_on(String name, String date) {
        Employee_Calendar cal = employeeMap.getOrDefault(name, employee).getCalendar();
        for (int i = 0; i < 10; i++) {
            cal.forceRegisterActivity(LocalDate.parse(date), "existing activity");
        }
    }

    @Given("{string} has {int} existing entries on {string}")
    public void has_existing_entries_on(String name, int count, String date) {
        Employee_Calendar cal = employeeMap.getOrDefault(name, employee).getCalendar();
        for (int i = 0; i < count; i++) {
            cal.forceRegisterActivity(LocalDate.parse(date), "existing activity");
        }
    }

    @When("{string} is added to activity {string}")
    public void is_added_to_activity(String name, String activityName) {
        Employee emloyee = employeeMap.getOrDefault(name, employee);
        try {
            project.addEmployeeToActivity(emloyee, emloyee, activityName);
        } catch (IllegalArgumentException | IllegalAccessError e) {
            errorMessageHandler.setErrorMessage(e.getMessage());
        }
    }

    @When("{string} adds {string} to activity {string}")
    public void adds_employee_to_activity(String actorName, String targetName, String activityName) {
        Employee actor = employeeMap.getOrDefault(actorName, employee);
        Employee target = employeeMap.getOrDefault(targetName, employee);
        try {
            project.addEmployeeToActivity(actor, target, activityName);
        } catch (IllegalArgumentException | IllegalAccessError e) {
            errorMessageHandler.setErrorMessage(e.getMessage());
        }
    }

    @Then("{string} is assigned to activity {string}")
    public void is_assigned_to_activity(String name, String activityName) {
        Employee emloyee = employeeMap.getOrDefault(name, employee);
        List<Employee> employees = project.getActivityFromName(activityName).getEmployees();
        assertTrue(employees.contains(emloyee));
    }

    @Then("{string} is not assigned to activity {string}")
    public void is_not_assigned_to_activity(String name, String activityName) {
        Employee emloyee = employeeMap.getOrDefault(name, employee);
        List<Employee> employees = project.getActivityFromName(activityName).getEmployees();
        assertFalse(employees.contains(emloyee));
    }

    @Given("there is a project with end date {string}")
    public void there_is_a_project_with_end_date(String endDate) {
        company.createProject("name", endDate);
        project = company.getProject("name");
    }

    @When("{string} changes the end date of activity {string} to {string}")
    public void changes_the_end_date_of_activity_to(String employeeName, String activityName, String newEndDate) {
        Employee emp = employeeMap.getOrDefault(employeeName, employee);
        try {
            project.changeActivityEndDate(emp, activityName, LocalDate.parse(newEndDate));
        } catch (IllegalAccessError e) {
            errorMessageHandler.setErrorMessage(e.getMessage());
        }
    }

    @When("{string} creates activity {string} from {string} to {string}")
    public void creates_activity_from_to(String employeeName, String activityName, String startDate, String endDate) {
        Employee emp = employeeMap.getOrDefault(employeeName, employee);
        try {
            project.createActivity(emp, activityName, "description", LocalDate.parse(startDate), LocalDate.parse(endDate));
        } catch (IllegalAccessError e) {
            errorMessageHandler.setErrorMessage(e.getMessage());
        }
    }

    @When("the project end date is reduced to {string}")
    public void the_project_end_date_is_reduced_to(String newEndDate) {
        project.setEndDate(newEndDate);
    }

    @Then("the activity {string} has end date {string}")
    public void the_activity_has_end_date(String activityName, String expectedEndDate) {
        Activity act = project.getActivityFromName(activityName);
        assertNotNull(act);
        assertEquals(LocalDate.parse(expectedEndDate), act.getEndDate());
    }
}
