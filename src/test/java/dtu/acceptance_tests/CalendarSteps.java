package dtu.acceptance_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.time.LocalDate;
import java.util.List;

import dtu.app.Company;
import dtu.app.Employee;
import dtu.app.employee_Calendar;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CalendarSteps {
    Company company;
    Employee employee;
    employee_Calendar calendar;

    @Given("the company exists")
    public void theCompanyExists() {
        company = new Company();
    }

    @When("an employee {string} is hired")
    public void anEmployeeIsHired(String string) {
        employee = new Employee(string);
        company.hireEmployee(employee);
    }

    @Then("{string} gets an personal calendar")
    public void getsAnPersonalCalendar(String string) {
        assertNotNull(employee);

        calendar = new employee_Calendar(string);
        employee.setCalendar(calendar);

        assertSame(calendar, employee.getCalendar()); // same object reference
        assertNotNull(company.getEmployee(string).getCalendar());
    }

    @When("{string} registers activity {string} on {string}")
    public void registersActivityOn(String employeeName, String activity, String date) {
        calendar.registerActivity(LocalDate.parse(date), activity);
    }

    @When("{string} registers time off {string} on {string}")
    public void registersTimeOffOn(String employeeName, String type, String date) {
        calendar.registerTimeOff(LocalDate.parse(date), type);
    }

    @Then("{string} has {int} calendar entries on {string}")
    public void hasCalendarEntriesOn(String employeeName, Integer expectedCount, String date) {
        List<employee_Calendar.CalendarEntry> entries = calendar.getEntries(LocalDate.parse(date));
        assertEquals(expectedCount.intValue(), entries.size());
    }

    @Then("the first calendar entry for {string} on {string} has type {string}")
    public void firstCalendarEntryHasType(String employeeName, String date, String expectedType) {
        List<employee_Calendar.CalendarEntry> entries = calendar.getEntries(LocalDate.parse(date));
        assertNotNull(entries);
        assertFalse(entries.isEmpty());
        assertEquals(expectedType, entries.get(0).getType().name());
    }

    @Then("the first calendar entry for {string} on {string} has description {string}")
    public void firstCalendarEntryHasDescription(String employeeName, String date, String expectedDescription) {
        List<employee_Calendar.CalendarEntry> entries = calendar.getEntries(LocalDate.parse(date));
        assertNotNull(entries);
        assertFalse(entries.isEmpty());
        assertEquals(expectedDescription, entries.get(0).getDescription());
    }
}
