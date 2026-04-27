package dtu.acceptance_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import dtu.app.Company;
import dtu.app.Employee;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class EmployeeSteps {
    Company company;
    Employee employee;
    ErrorMessageHandler errorMessageHandler;
    Employee foundEmployee;
    public EmployeeSteps(Company company, ErrorMessageHandler errorMessageHandler) {
        this.company = company;
        this.errorMessageHandler = errorMessageHandler;
    }

    @When("{string} is seached for")
    public void isSeachedFor(String string) {
        foundEmployee = company.getEmployeeFromName(string);  
    }
        @Then("{string} is found")
    public void isFound(String string) {
        assertEquals(string, foundEmployee.getName());
    }

    @Then("{string} is not found")
    public void is_not_found(String s) {
        assertNull(foundEmployee);
    }

    @Then("{string} is logged in")
    public void is_logged_in(String s) {
        assertEquals(s, company.getLoggedIn().getName());
    }

    @When("{string} logs in with initals {string}")
    public void logs_in_with_initals(String s, String s2) {
        try {
            company.login(s2);
        } catch (IllegalAccessError e) {
            errorMessageHandler.setErrorMessage(e.getMessage());
        }

    }
}
