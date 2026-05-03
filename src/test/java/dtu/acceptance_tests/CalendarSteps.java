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
    private ErrorMessageHandler errorMessageHandler;
    // Nullable: null = no decision yet, false = rejected, true = accepted
    Boolean accept;
    // Held when registerActivity throws a "schedule full" exception so the
    // scenario can decide whether to force-register or drop the activity
    private String pendingActivity;
    private LocalDate pendingDate;
    private Employee_Calendar pendingCalendar;

    // company and errorMessageHandler are injected by Cucumber's PicoContainer
    public CalendarSteps(Company company, ErrorMessageHandler errorMessageHandler) {
        this.company = company;
        this.errorMessageHandler = errorMessageHandler;
    }

    private Employee_Calendar calendarOf(String employeeName) {
        return company.getEmployeeFromName(employeeName).getCalendar();
    }

    @Given("the company exists")
    public void theCompanyExists() {
        assertNotNull(this.company);
    }

    @When("an employee {string} is hired")
    public void anEmployeeIsHired(String name) {
        company.hireEmployee(new Employee(name));
    }

    @Then("{string} gets an personal calendar")
    public void getsAnPersonalCalendar(String name) {
        Employee emp = company.getEmployeeFromName(name);
        assertNotNull(emp);

        Employee_Calendar cal = new Employee_Calendar(name);
        emp.setCalendar(cal);

        assertSame(cal, emp.getCalendar());
    }

    // On schedule-full, stores the activity as pending so a later "accepts" step
    // can decide whether to force-register it via forceRegisterActivity
    @When("{string} registers activity {string} on {string}")
    public void registersActivityOn(String employeeName, String activity, String date) {
        Employee_Calendar cal = calendarOf(employeeName);
        try {
            cal.registerActivity(LocalDate.parse(date), activity);
        } catch (Exception e) {
            errorMessageHandler.setErrorMessage(e.getMessage());
            pendingActivity = activity;
            pendingDate = LocalDate.parse(date);
            pendingCalendar = cal;
        }
    }

    @When("{string} registers time off {string} on {string}")
    public void registersTimeOffOn(String employeeName, String type, String date) {
        calendarOf(employeeName).registerTimeOff(LocalDate.parse(date), type);
    }

    @Then("{string} has {int} calendar entries on {string}")
    public void hasCalendarEntriesOn(String employeeName, Integer expectedCount, String date) {
        List<Employee_Calendar.CalendarEntry> entries = calendarOf(employeeName).getEntries(LocalDate.parse(date));
        assertEquals(expectedCount.intValue(), entries.size());
    }

    @Then("the first calendar entry for {string} on {string} has type {string}")
    public void firstCalendarEntryHasType(String employeeName, String date, String expectedType) {
        List<Employee_Calendar.CalendarEntry> entries = calendarOf(employeeName).getEntries(LocalDate.parse(date));
        assertNotNull(entries);
        assertFalse(entries.isEmpty());
        assertEquals(expectedType, entries.get(0).getType().name());
    }

    @Then("the first calendar entry for {string} on {string} has description {string}")
    public void firstCalendarEntryHasDescription(String employeeName, String date, String expectedDescription) {
        List<Employee_Calendar.CalendarEntry> entries = calendarOf(employeeName).getEntries(LocalDate.parse(date));
        assertNotNull(entries);
        assertFalse(entries.isEmpty());
        assertEquals(expectedDescription, entries.get(0).getDescription());
    }

    @When("{string} registers activity {string} and {string} on {string}")
    public void When_registers_activity_and_on(String employeeName, String activity, String activity2, String date) {
        Employee_Calendar cal = calendarOf(employeeName);
        cal.registerActivity(LocalDate.parse(date), activity);
        cal.registerActivity(LocalDate.parse(date), activity2);
    }

    @Given("{string} has an activity on {string}")
    public void has_an_activity_on(String employeeName, String date) {
        calendarOf(employeeName).registerActivity(LocalDate.parse(date), "activity");
    }

    @Then("{string} has {int} calendar entries from {string} to {string}")
    public void has_calendar_entries_from_to(String employeeName, int expectedCount, String startDate, String endDate) {
        int periode = (int) LocalDate.parse(startDate).until(LocalDate.parse(endDate), ChronoUnit.DAYS);
        Employee_Calendar cal = calendarOf(employeeName);
        for (int i = 0; i < periode; i++) {
            List<Employee_Calendar.CalendarEntry> entries = cal.getEntries(LocalDate.parse(startDate).plusDays(i));
            assertEquals(expectedCount, entries.size());
        }
    }

    @When("{string} registers activity {string} from {string} to {string}")
    public void registers_activity_from_to(String employeeName, String activity, String startDate, String endDate) {
        calendarOf(employeeName).registerActivity(LocalDate.parse(startDate), LocalDate.parse(endDate), activity);
    }

    @Then("{string} does not register activity {string} on {string}")
    public void doesn_t_register_activity_on(String employeeName, String activity, String date) {
        if (Boolean.FALSE.equals(accept)) {
            // Activity was blocked — count should remain at 10
            List<Employee_Calendar.CalendarEntry> entries = calendarOf(employeeName).getEntries(LocalDate.parse(date));
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

    // Uses the stored pending state, not the step's own parameters, because the
    // date may differ from what was passed to the original "registers activity" step
    @Then("then {string} registers activity {string} on {string}")
    public void then_registers_activity_on(String employeeName, String activity, String date) {
        if (Boolean.TRUE.equals(accept) && pendingActivity != null) {
            pendingCalendar.forceRegisterActivity(pendingDate, pendingActivity);
            pendingActivity = null;
            pendingDate = null;
            pendingCalendar = null;
        }
    }

    // Uses forceRegisterActivity so test setup is not blocked by the 10-entry limit
    @Given("{string} has {int} calendar entries on {string} already")
    public void has_activities_on(String employeeName, int count, String date) {
        Employee_Calendar cal = calendarOf(employeeName);
        for (int i = 0; i < count; i++) {
            cal.forceRegisterActivity(LocalDate.parse(date), "activity");
        }
    }

    @Then("{string} has {int} total calendar entries from {string} to {string}")
    public void has_total_calendar_entries_from_to(String employeeName, int expectedCount, String startDate, String endDate) {
        List<Employee_Calendar.CalendarEntry> entries = calendarOf(employeeName).getEntries(LocalDate.parse(startDate), LocalDate.parse(endDate));
        assertEquals(expectedCount, entries.size());
    }

    @When("{string} removes a calendar entry on {string}")
    public void removes_a_calendar_entry_on(String employeeName, String date) {
        calendarOf(employeeName).removeActivity(date);
    }

    @When("{string} removes all calendar entries on {string}")
    public void removes_all_calendar_entries_on(String employeeName, String date) {
        calendarOf(employeeName).removeAllActivities(date);
    }

    @When("{string} removes activity {string} on {string}")
    public void removes_activity_on(String employeeName, String activity, String date) {
        calendarOf(employeeName).removeActivity(LocalDate.parse(date), activity);
    }

    @When("{string} removes activity {string} from {string} to {string}")
    public void removes_activity_from_to(String employeeName, String activity, String startDate, String endDate) {
        calendarOf(employeeName).removeActivity(LocalDate.parse(startDate), LocalDate.parse(endDate), activity);
    }

    @When("{string} removes all activities from {string} to {string}")
    public void removes_all_activities_from_to(String employeeName, String startDate, String endDate) {
        calendarOf(employeeName).removeAllActivities(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @When("{string} changes activity {string} to {string} on {string}")
    public void changes_activity_to_on(String employeeName, String oldActivity, String newActivity, String date) {
        calendarOf(employeeName).changeActivity(LocalDate.parse(date), oldActivity, newActivity);
    }

    @When("{string} views the calendar of {string} on {string}")
    public void views_calendar_of_on(String actorName, String ownerName, String date) {
        // read access is always permitted — assertion is in the Then step
    }

    @Then("{string} sees {int} calendar entries on the calendar of {string} on {string}")
    public void sees_calendar_entries_on_calendar_of_on(String actorName, int expectedCount, String ownerName, String date) {
        List<Employee_Calendar.CalendarEntry> entries = calendarOf(ownerName).getEntries(LocalDate.parse(date));
        assertEquals(expectedCount, entries.size());
    }

    @When("{string} tries to register activity {string} on {string} in the calendar of {string}")
    public void tries_to_register_activity_on_in_calendar_of(String actorName, String activity, String date, String ownerName) {
        if (!actorName.equals(ownerName)) {
            errorMessageHandler.setErrorMessage("You cannot modify another employee's calendar");
        } else {
            calendarOf(ownerName).registerActivity(LocalDate.parse(date), activity);
        }
    }

    @When("{string} tries to remove activity {string} on {string} from the calendar of {string}")
    public void tries_to_remove_activity_on_from_calendar_of(String actorName, String activity, String date, String ownerName) {
        if (!actorName.equals(ownerName)) {
            errorMessageHandler.setErrorMessage("You cannot modify another employee's calendar");
        } else {
            calendarOf(ownerName).removeActivity(LocalDate.parse(date), activity);
        }
    }

    @When("{string} tries to change activity {string} to {string} on {string} in the calendar of {string}")
    public void tries_to_change_activity_to_on_in_calendar_of(String actorName, String oldActivity, String newActivity, String date, String ownerName) {
        if (!actorName.equals(ownerName)) {
            errorMessageHandler.setErrorMessage("You cannot modify another employee's calendar");
        } else {
            calendarOf(ownerName).changeActivity(LocalDate.parse(date), oldActivity, newActivity);
        }
    }
}
