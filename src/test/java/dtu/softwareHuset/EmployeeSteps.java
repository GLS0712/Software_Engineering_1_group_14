package dtu.softwareHuset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import dtu.softwareHuset.app.Company;
import dtu.softwareHuset.app.Employee;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class EmployeeSteps {
    Company company;
    Employee employee;
    ErrorMessageHandler errorMessageHandler;
    Employee foundEmployee;
    // Author: Daniel Hedegaard
    public EmployeeSteps(Company company, ErrorMessageHandler errorMessageHandler) {
        this.company = company;
        this.errorMessageHandler = errorMessageHandler;
    }

    @When("{string} is seached for")
    // Author: Daniel Hedegaard
    public void isSeachedFor(String string) {
        foundEmployee = company.getEmployeeFromName(string);  
    }
        @Then("{string} is found")
    // Author: Daniel Hedegaard
    public void isFound(String string) {
        assertEquals(string, foundEmployee.getName());
    }

    @Then("{string} is not found")
    // Author: Daniel Hedegaard
    public void is_not_found(String s) {
        assertNull(foundEmployee);
    }

    @Then("{string} is logged in")
    // Author: Daniel Hedegaard
    public void is_logged_in(String s) {
        assertEquals(s, company.getLoggedIn().getName());
    }

    @When("{string} logs in with initals {string}")
    // Author: Daniel Hedegaard
    public void logs_in_with_initals(String s, String s2) {
        try {
            company.login(s2);
        } catch (IllegalAccessError e) {
            errorMessageHandler.setErrorMessage(e.getMessage());
        }

    }

    @Given("there is an employee named {string} with initals {string} in company")
    // Author: Daniel Hedegaard
    public void there_is_an_employee_named_with_initals_in_company(String name, String initals) {
        this.employee = new Employee(name);
        company.hireEmployee(employee);
        company.setInitailsForEmployee(employee, initals);
    }

    @When("initials {string} is seached for")
    // Author: Daniel Hedegaard
    public void initials_is_seached_for(String initals) {
        foundEmployee = company.getEmployeeFromInitials(initals);
    }

}
