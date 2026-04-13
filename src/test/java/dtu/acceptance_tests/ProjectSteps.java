package dtu.acceptance_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import dtu.app.Company;
import dtu.app.Employee;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ProjectSteps {

    Company company = new Company();
    Employee employee;

    @When("employee creates project with {string} and {string}")
    public void employeeCreatesProjectWithAnd(String string1, String string2) {
        company.createProject(string1, string2);
    }

    @Then("there is a project named {string} with {string}")
    public void thereIsAProjectNamedWith(String string1, String string2) {
        assertNotNull(company.getProject(string1));
        assertEquals(string2, company.getProject(string1).getTime());
    }

    @Then("there is a project named {string}")
    public void there_is_a_project_named(String s) {
        assertNotNull(company.getProject(s));
    }

    @When("employee creates project with {string}")
    public void employee_creates_project_with(String s) {
        company.createProject(s);
    }

    @Then("there is a project named {string} with {string} and {string} is projectLeader")
    public void there_is_a_project_named_with_and_is_projectLeader(String s, String s2, String s3) {
        assertNotNull(company.getProject(s));
        assertEquals(s2, company.getProject(s).getTime());
        assertEquals(s3, company.getProject(s).getProjectLeader().getName());
    }

    @When("John doe creates project with {string}, {string} and {string} as projectLeader")
    public void John_doe_creates_project_with_and_as_projectLeader(String s, String s2, String s3) {
        company.createProject(s, s2, employee);
    }

    @Given("there is an employee named {string}")
    public void there_is_an_employee_named(String s) {
        employee = new Employee("John doe");
    }

}
