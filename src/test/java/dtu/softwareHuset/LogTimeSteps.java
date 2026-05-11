package dtu.softwareHuset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dtu.softwareHuset.app.Activity;
import dtu.softwareHuset.app.Company;
import dtu.softwareHuset.app.Employee;
import dtu.softwareHuset.app.Project;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;

public class LogTimeSteps {

    Company company;
    Project project;
    Activity activity;
    Employee employee;
    LocalDate date = LocalDate.of(1111, 11, 11);
    Map<String, Employee> employeeMap = new HashMap<>();
    ErrorMessageHandler errorMessageHandler;

    // Author: GubbeMK
    public LogTimeSteps(Company company, ErrorMessageHandler errorMessageHandler) {
        this.company = company;
        this.errorMessageHandler = errorMessageHandler;
    }

    @Given("employee {string} is assigned to activity {string} in project {string}")
    // Author: GubbeMK
    public void employeeIsAssignedToActivityInProject(String employeeName, String activityName, String projectName) {
        employee = new Employee(employeeName, employeeName);
        company.hireEmployee(employee);
        company.createProject(projectName);
        project = company.getProject(projectName);
        project.createActivity(employee, activityName, "");
        activity = project.getActivityFromName(activityName);

        activity.addEmployee(employee);
    }

    @When("{string} logs {double} hours on {string}")
    // Author: GubbeMK
    public void logsHoursOn(String employeeName, double hours, String activityName) throws IOException {
        company.registerLog(employee, project, activity, date, hours);
    }

    @Then("{double} hours should be registered on {string} for {string}")
    // Author: GubbeMK
    public void hoursShouldBeRegisteredOnFor(double hours, String acitvityName, String employeeName)
            throws IOException {
        assertEquals(employeeName, company.loadAllLogs().getLast().get(1));
        assertEquals(acitvityName, company.loadAllLogs().getLast().get(3));
        assertEquals(String.valueOf(hours), company.loadAllLogs().getLast().get(5));
    }

    @When("{string} logs {double} hours on {string} on {string}")
    // Author: GubbeMK
    public void logsHoursOnOn(String string, double hours, String string2, String string3) throws IOException {
        company.registerLog(employee, project, activity, date, hours);
    }

    @Then("{string}'s time sheet for {string} shows {double} hours on {string}")
    // Author: GubbeMK
    public void sTimeSheetForShowsHoursOn(String employeeName, String expectedDate, double hours, String acitvityName)
            throws IOException {
        assertEquals(employeeName, company.loadAllLogs().getLast().get(1));
        assertEquals(acitvityName, company.loadAllLogs().getLast().get(3));
        assertEquals(expectedDate, company.loadAllLogs().getLast().get(4));
        assertEquals(String.valueOf(hours), company.loadAllLogs().getLast().get(5));
    }

    @Given("activity {string} exists in project {string}")
    // Author: GubbeMK
    public void activityExistsInProject(String activityName, String projectName) {
        company.createProject(projectName);
        project = company.getProject(projectName);
        project.createActivity(employee, activityName, "");
        activity = project.getActivityFromName(activityName);
    }

    @Given("employee {string} is not assigned to {string}")
    // Author: GubbeMK
    public void employeeIsNotAssignedTo(String employeeName, String activityName) {
        employee = new Employee(employeeName, employeeName);
        company.hireEmployee(employee);
    }

    @Given("{string} has logged {double} hours on {string} in {string}")
    // Author: GubbeMK
    public void hasLoggedHoursOnIn(String employeeName, double hours, String activityName, String projectName)
            throws IOException {
        employee = new Employee(employeeName, employeeName);
        company.hireEmployee(employee);
        company.createProject(projectName);
        project = company.getProject(projectName);
        project.createActivity(employee, activityName, "");
        activity = project.getActivityFromName(activityName);
        activity.addEmployee(employee);

        company.registerLog(employee, project, activity, date, hours);
    }

    @When("{string} updates the entry to {double} hours")
    // Author: GubbeMK
    public void updatesTheEntryToHours(String employeeName, double hours) throws IOException {
        List<String> log = company.getLog(company.loadAllLogs().size() - 1);
        log.set(5, String.valueOf(hours));
        company.changeLog(company.loadAllLogs().size() - 1, log);
    }

    @Then("{double} hours should now be registered on {string} for {string}")
    // Author: GubbeMK
    public void hoursShouldNowBeRegisteredOnFor(Double hours, String activityName, String employeeName)
            throws IOException {
        assertEquals(String.valueOf(hours), company.loadAllLogs().getLast().get(5));
    }

    @When("{string} tries to log {double} hours on {string}")
    // Author: GubbeMK
    public void triesToLogHoursOn(String employeeName, double hours, String activityName) throws IOException {
        try {
            company.registerLog(employee, project, activity, date, hours);
        } catch (Exception e) {
            errorMessageHandler.setErrorMessage(e.getMessage());
        }
    }

    @Then("the system should reject the entry with error: {string}")
    // Author: GubbeMK
    public void theSystemShouldRejectTheEntryWithError(String error) {
        assertEquals(errorMessageHandler.getErrorMessage(), error);
    }

    @Then("no time should be registered for {string} on {string}")
    // Author: GubbeMK
    public void noTimeShouldBeRegisteredForOn(String employeeName, String activityName) throws IOException {
        assertNotEquals(company.loadAllLogs().getLast().get(1), employeeName);
    }

    @Given("today is {string}")
    // Author: GubbeMK
    public void todayIs(String todaysDate) {
        date = LocalDate.parse(todaysDate);
    }

    @When("{string} tries to log {double} hours on {string} on {string}")
    // Author: GubbeMK
    public void triesToLogHoursOnOn(String employeeName, double hours, String activityName, String dateEx) throws IOException {
        try {
            company.registerLog(employee, project, activity, LocalDate.parse(dateEx), hours);
        } catch (Exception e) {
            errorMessageHandler.setErrorMessage(e.getMessage());
        }
    }

    @Then("no time should be registered for {string}")
    // Author: GubbeMK
    public void noTimeShouldBeRegisteredFor(String employeeName) throws IOException {
        assertNotEquals(employeeName, company.loadAllLogs().getLast().get(1));
    }

    
}