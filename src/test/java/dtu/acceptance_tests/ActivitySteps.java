package dtu.acceptance_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import dtu.app.Company;
import dtu.app.Employee;
import dtu.app.Project;
import dtu.app.Activity;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ActivitySteps {

    Company company = new Company();
    Project project;
    Activity activity;
    Employee employee;
    ErrorMessageHandler errorMessageHandler = new ErrorMessageHandler();

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
}
