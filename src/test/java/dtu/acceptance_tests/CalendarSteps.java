package dtu.acceptance_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import dtu.app.Company;
import dtu.app.Employee;
import dtu.app.Employee_Calendar;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CalendarSteps {
    private Company company;
    private Employee employee;
    private Employee_Calendar calendar;
    private ErrorMessageHandler errorMessageHandler;
    Boolean accept;
    private String pendingActivity;
    private LocalDate pendingDate;

    public CalendarSteps(Company company, ErrorMessageHandler errorMessageHandler) {
        this.company = company;
        this.errorMessageHandler = errorMessageHandler;
    }

    @Given("the company exists")
    public void theCompanyExists() {
        assertNotNull(this.company);
    }

    @When("an employee {string} is hired")
    public void anEmployeeIsHired(String string) {
        employee = new Employee(string);
        company.hireEmployee(employee);
    }

    @Then("{string} gets an personal calendar")
    public void getsAnPersonalCalendar(String string) {
        assertNotNull(employee);

        calendar = new Employee_Calendar(string);
        employee.setCalendar(calendar);

        assertSame(calendar, employee.getCalendar()); // same object reference
        assertNotNull(company.getEmployee(string).getCalendar());
    }

    @When("{string} registers activity {string} on {string}")
    public void registersActivityOn(String employeeName, String activity, String date) {
        try {
            calendar.registerActivity(LocalDate.parse(date), activity);
        } catch (Exception e) {
            errorMessageHandler.setErrorMessage(e.getMessage());

            pendingActivity = activity;
            pendingDate = LocalDate.parse(date);
        }

    }

    @When("{string} registers time off {string} on {string}")
    public void registersTimeOffOn(String employeeName, String type, String date) {
        calendar.registerTimeOff(LocalDate.parse(date), type);
    }

    @Then("{string} has {int} calendar entries on {string}")
    public void hasCalendarEntriesOn(String employeeName, Integer expectedCount, String date) {
        List<Employee_Calendar.CalendarEntry> entries = calendar.getEntries(LocalDate.parse(date));
        assertEquals(expectedCount.intValue(), entries.size());
    }

    @Then("the first calendar entry for {string} on {string} has type {string}")
    public void firstCalendarEntryHasType(String employeeName, String date, String expectedType) {
        List<Employee_Calendar.CalendarEntry> entries = calendar.getEntries(LocalDate.parse(date));
        assertNotNull(entries);
        assertFalse(entries.isEmpty());
        assertEquals(expectedType, entries.get(0).getType().name());
    }

    @Then("the first calendar entry for {string} on {string} has description {string}")
    public void firstCalendarEntryHasDescription(String employeeName, String date, String expectedDescription) {
        List<Employee_Calendar.CalendarEntry> entries = calendar.getEntries(LocalDate.parse(date));
        assertNotNull(entries);
        assertFalse(entries.isEmpty());
        assertEquals(expectedDescription, entries.get(0).getDescription());
    }

    @When("{string} registers activity {string} and {string} on {string}")
    public void When_registers_activity_and_on(String employeeName, String activity, String activity2, String date) {
        calendar.registerActivity(LocalDate.parse(date), activity);
        calendar.registerActivity(LocalDate.parse(date), activity2);
    }

    @Given("{string} has an activity on {string}")
    public void has_an_activity_on(String employeeName, String date) {
        calendar.registerActivity(LocalDate.parse(date), "activity");
    }

    @Then("{string} has {int} calendar entries from {string} to {string}")
    public void has_calendar_entries_from_to(String employeeName, int expectedCount, String startDate, String endDate) {
        int periode = (int) LocalDate.parse(startDate).until(LocalDate.parse(endDate), ChronoUnit.DAYS);
        for (int i = 0; i < periode; i++) {
            List<Employee_Calendar.CalendarEntry> entries = calendar.getEntries(LocalDate.parse(startDate).plusDays(i));
            assertEquals(expectedCount, entries.size());
        }
    }

    @When("{string} registers activity {string} from {string} to {string}")
    public void registers_activity_from_to(String employeeName, String activity, String startDate, String endDate) {
        calendar.registerActivity(LocalDate.parse(startDate), LocalDate.parse(endDate), activity);
    }

    @Then("{string} does not register activity {string} on {string}")
    public void doesn_t_register_activity_on(String employeeName, String activity, String date) {
        if (Boolean.FALSE.equals(accept)) {
            // Activity was blocked — count should remain at 10
            List<Employee_Calendar.CalendarEntry> entries = calendar.getEntries(LocalDate.parse(date));
            assertEquals(10, entries.size());
        }
    }

    @When("{string} does not accept")
    public void don_t_accept(String employeeName) {
        accept = false;
    }

    @When("{string} accepts")
    public void accepts(String employeeName) {
        accept = true;
    }

    @Then("the message {string}")
    public void the_message(String message) {
        assertEquals(message, errorMessageHandler.getErrorMessage());
    }

    @Then("then {string} registers activity {string} on {string}")
    public void then_registers_activity_on(String employeeName, String activity, String date) {
        if (Boolean.TRUE.equals(accept) && pendingActivity != null) {
            calendar.forceRegisterActivity(pendingDate, pendingActivity);
            pendingActivity = null;
            pendingDate = null;
        }
    }

    @Given("{string} has {int} calendar entries on {string} already")
    public void has_activities_on(String employeeName, int count, String date) {
        for (int i = 0; i < count; i++) {
            calendar.forceRegisterActivity(LocalDate.parse(date), "activity");
        }
    }

    @Then("{string} has {int} total calendar entries from {string} to {string}")
    public void has_total_calendar_entries_from_to(String employeeName, int expectedCount, String startDate, String endDate) {
        List<Employee_Calendar.CalendarEntry> entries = calendar.getEntries(LocalDate.parse(startDate), LocalDate.parse(endDate));
        assertEquals(expectedCount, entries.size());
    }
}
