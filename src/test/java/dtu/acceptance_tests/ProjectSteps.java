package dtu.acceptance_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import dtu.app.Company;
import dtu.app.Employee;
import dtu.app.Project;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ProjectSteps {

    Company company;
    Employee employee;
    Project project;

    public ProjectSteps(Company company){
        this.company = company;
    }

    @Given("there is an employee named {string}")
    public void there_is_an_employee_named(String s) {
        employee = new Employee(s);
    }

    @When("{string} creates project with name {string} with end date {string}")
    public void employee_creates_project_with_with_end_date(String employeeName, String projectName, String endDate) {
        company.createProject(projectName, endDate);
        project = company.getProject(projectName);
    }

    @Then("the project named {string} has no projectLeader")
    public void the_project_named_has_no_projectLeader(String projectName) {
        assertNull(company.getProject(projectName).getProjectLeader());
    }

    @Then("there is a project named {string} with an end date {string}")
    public void there_is_a_project_named_with_an_end_date(String projectName, String endDate) {
        assertNotNull(company.getProject(projectName));
        assertEquals(endDate, company.getProject(projectName).getEndDate());
    }

    @Then("there is a project named {string} with no end date")
    public void there_is_a_project_named_with_no_end_date(String projectName) {
        assertNotNull(company.getProject(projectName));
        assertNull(company.getProject(projectName).getEndDate());
    }

    @When("{string} creates project with name {string} without end date")
    public void employee_creates_project_with_name_without_end_date(String employeeName, String projectName) {
        company.createProject(projectName);
        project = company.getProject(projectName);
    }

    @Then("the project named {string} has the projectLeader {string}")
    public void the_project_named_has_the_projectLeader(String projectName, String employeeName) {
        assertEquals(employeeName, company.getProject(projectName).getProjectLeader().getName());
    }

    @When("{string} is assigned as projectLeader")
    public void is_assigned_as_projectLeader(String employeeName) {
        project.setProjectLeader(employee);
    }

}
