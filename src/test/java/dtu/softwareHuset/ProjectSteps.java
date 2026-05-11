package dtu.softwareHuset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;

import dtu.softwareHuset.app.Company;
import dtu.softwareHuset.app.Employee;
import dtu.softwareHuset.app.Project;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ProjectSteps {

    Company company;
    Employee employee;
    Project project;

    // Author: Daniel Hedegaard
    public ProjectSteps(Company company){
        this.company = company;
    }

    @Given("there is an employee named {string}")
    // Author: GedeGustav
    public void there_is_an_employee_named(String s) {
        employee = new Employee(s);
    }

    @When("{string} creates project with name {string} with end date {string}")
    // Author: GedeGustav
    public void employee_creates_project_with_with_end_date(String employeeName, String projectName, String endDate) {
        company.createProject(projectName, endDate);
        project = company.getProject(projectName);
    }

    @Then("the project named {string} has no projectLeader")
    // Author: GedeGustav
    public void the_project_named_has_no_projectLeader(String projectName) {
        assertNull(company.getProject(projectName).getProjectLeader());
    }

    @Then("there is a project named {string} with an end date {string}")
    // Author: GedeGustav
    public void there_is_a_project_named_with_an_end_date(String projectName, String endDate) {
        assertNotNull(company.getProject(projectName));
        assertEquals(endDate, company.getProject(projectName).getEndDate());
    }

    @Then("there is a project named {string} with no end date")
    // Author: GedeGustav
    public void there_is_a_project_named_with_no_end_date(String projectName) {
        assertNotNull(company.getProject(projectName));
        assertNull(company.getProject(projectName).getEndDate());
    }

    @When("{string} creates project with name {string} without end date")
    // Author: GedeGustav
    public void employee_creates_project_with_name_without_end_date(String employeeName, String projectName) {
        company.createProject(projectName);
        project = company.getProject(projectName);
    }

    @Then("the project named {string} has the projectLeader {string}")
    // Author: GedeGustav
    public void the_project_named_has_the_projectLeader(String projectName, String employeeName) {
        assertEquals(employeeName, company.getProject(projectName).getProjectLeader().getName());
    }

    @When("{string} is assigned as projectLeader")
    // Author: GedeGustav
    public void is_assigned_as_projectLeader(String employeeName) {
        project.setProjectLeader(employee);
    }

    @Given("there is another project with an id")
    // Author: Daniel Hedegaard
    public void there_is_another_project_with_an_id() {
       company.createProject("project 2", "02.02.2026");
       company.getProject("project 2").setId(company);
    }  

    @Then("there is a project named {string} with id {string}")
    // Author: Daniel Hedegaard
    public void there_is_a_project_named_with_id(String projectName, String Id) {
        assertNotNull(company.getProject(projectName));
        assertEquals(Id, company.getProject(projectName).getId());
    }


    @Given("there is no other projects")
    // Author: Daniel Hedegaard
    public void there_is_no_other_projects() {
        company.getProjects().clear();
    }

    @When("{string} sets the id for {string}")
    // Author: Daniel Hedegaard
    public void sets_the_id_for(String employeeName, String projectName) {
        company.getProject(projectName).setId(company);

    }

    @When("{string} creates project with name {string} with end date {string} and project leader {string}")
    // Author: Daniel Hedegaard
    public void creates_project_with_name_with_end_date_and_projectLeader(String name, String projectName, String endDate , String projectleader) {
        company.createProject(projectName, endDate, employee);
        project = company.getProject(projectName);
    }

    @Then("the projects Description is {string}")
    // Author: Daniel Hedegaard
    public void the_projects_Description_is(String expectedDescription) {
        assertEquals(expectedDescription, project.getDescription());
    }

    @When("{string} set projects Description to {string}")
    // Author: Daniel Hedegaard
    public void set_projects_Description_to(String employeeName, String description) {
        project.setDescription(description);
    }

    @Then("the projects name is {string}")
    // Author: Daniel Hedegaard
    public void the_projects_name_is(String expectedName) {
        assertEquals(expectedName, project.getName());
    }

    @When("{string} set projects name to {string}")
    // Author: Daniel Hedegaard
    public void set_projects_name_to(String employeeName, String nameString) {
       project.setName(nameString);
    }

    @Then("the projects startDate is {string}")
    // Author: Daniel Hedegaard
    public void the_projects_startDate_is(String expectedDate) {
        assertEquals(expectedDate, project.getStartDate().toString());
    }

    @When("{string} set projects startDate to {string}")
    // Author: Daniel Hedegaard
    public void set_projects_startDate_to(String employeeName, String startDate) {
        project.setStartDate(LocalDate.parse(startDate));
    }

    @When("{string} sets the project end date to {string}")
    // Author: GedeGustav
    public void sets_the_project_end_date_to(String employeeName, String endDate) {
        project.setEndDate(endDate);
    }

    @Then("the projects end date is {string}")
    // Author: GedeGustav
    public void the_projects_end_date_is(String expectedEndDate) {
        assertEquals(expectedEndDate, project.getEndDate());
    }

    @When("the project leader is removed")
    // Author: GedeGustav
    public void the_project_leader_is_removed() {
        project.setProjectLeader(null);
    }

}
