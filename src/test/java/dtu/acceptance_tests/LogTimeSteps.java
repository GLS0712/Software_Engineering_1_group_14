package dtu.acceptance_tests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dtu.app.Activity;
import dtu.app.Company;
import dtu.app.Employee;
import dtu.app.Project;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

public class LogTimeSteps {

    Company company;
    Project project;
    Activity activity;
    Employee employee;
    LocalDate date = LocalDate.of(1111, 11, 11);
    Map<String, Employee> employeeMap = new HashMap<>();
    ErrorMessageHandler errorMessageHandler;

    public LogTimeSteps(Company company, ErrorMessageHandler errorMessageHandler) {
        this.company = company;
        this.errorMessageHandler = errorMessageHandler;
    }

    @Given("employee {string} is assigned to activity {string} in project {string}")
    public void employeeIsAssignedToActivityInProject(String employeeName, String activityName, String projectName) {
        employee = new Employee(employeeName);
        company.createProject(projectName);
        project = company.getProject(projectName);
        project.createActivity(employee, activityName, "");
        activity = project.getActivityFromName(activityName);

        activity.addEmployee(employee);
    }

    @When("{string} logs {double} hours on {string}")
    public void logsHoursOn(String employeeName, double hours, String activityName) throws IOException {
        company.registerLog(employee, project, activity, date, hours);
    }

    @Then("{double} hours should be registered on {string} for {string}")
    public void hoursShouldBeRegisteredOnFor(double hours, String acitvityName, String employeeName)
            throws IOException {
        assertEquals(company.loadAllLogs().getLast().get(1), employeeName);
        assertEquals(company.loadAllLogs().getLast().get(3), acitvityName);
        assertEquals(company.loadAllLogs().getLast().get(5), String.valueOf(hours));
    }

    @When("{string} logs {double} hours on {string} on {string}")
    public void logsHoursOnOn(String string, double hours, String string2, String string3) throws IOException {
        company.registerLog(employee, project, activity, date, hours);
    }

    @Then("{string}'s time sheet for {string} shows {double} hours on {string}")
    public void sTimeSheetForShowsHoursOn(String employeeName, String expectedDate, double hours, String acitvityName)
            throws IOException {
        assertEquals(company.loadAllLogs().getLast().get(1), employeeName);
        assertEquals(company.loadAllLogs().getLast().get(3), acitvityName);
        assertEquals(company.loadAllLogs().getLast().get(4), expectedDate);
        assertEquals(company.loadAllLogs().getLast().get(5), String.valueOf(hours));
    }

    @Given("activity {string} exists in project {string}")
    public void activityExistsInProject(String activityName, String projectName) {
        company.createProject(projectName);
        project = company.getProject(projectName);
        project.createActivity(employee, activityName, "");
        activity = project.getActivityFromName(activityName);
    }

    @Given("employee {string} is not assigned to {string}")
    public void employeeIsNotAssignedTo(String employeeName, String activityName) {
        employee = new Employee(employeeName);
    }

    @Given("{string} has logged {double} hours on {string} in {string}")
    public void hasLoggedHoursOnIn(String employeeName, double hours, String activityName, String projectName)
            throws IOException {
        employee = new Employee(employeeName);
        company.createProject(projectName);
        project = company.getProject(projectName);
        project.createActivity(employee, activityName, "");
        activity = project.getActivityFromName(activityName);
        activity.addEmployee(employee);

        company.registerLog(employee, project, activity, date, hours);
    }

    @When("{string} updates the entry to {double} hours")
    public void updatesTheEntryToHours(String employeeName, double hours) throws IOException {
        List<String> log = company.getLog(company.loadAllLogs().size()-1);
        log.set(5, String.valueOf(hours));
        company.changeLog(company.loadAllLogs().size()-1, log);
    }

    @Then("{double} hours should now be registered on {string} for {string}")
    public void hoursShouldNowBeRegisteredOnFor(Double hours, String activityName, String employeeName) throws IOException {
        assertEquals(company.loadAllLogs().getLast().get(5), String.valueOf(hours));
    }
}