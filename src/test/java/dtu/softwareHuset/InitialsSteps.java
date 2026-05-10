package dtu.softwareHuset;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertNull;

import dtu.softwareHuset.app.Company;
import dtu.softwareHuset.app.Employee;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class InitialsSteps {
    Company company;
    Employee employee;
    ErrorMessageHandler errorMessageHandler;

    public InitialsSteps(Company company, ErrorMessageHandler errorMessageHandler) {
        this.company = company;
        this.errorMessageHandler = errorMessageHandler;
    }

    @Given("there is an employee named {string} in company")
    public void thereIsAnEmployeeNamedInCompany(String string) {
        this.employee = new Employee(string);
        company.hireEmployee(employee);
    }

    @Given("{string} does not have any intials")
    public void doesNotHaveAnyIntials(String string) {
        assertNull(employee.getInitials());
    }

    @When("{string} sets initials as {string}")
    public void sets_initials_as(String s, String s2) {
        try {
            company.setInitailsForEmployee(employee, "jodo");
        } catch (IllegalAccessError e) {
            errorMessageHandler.setErrorMessage(e.getMessage());
        }

    }

    @Then("his initials are {string}")
    public void hisInitialsAre(String string) {
        assertEquals(string, company.getEmployeeFromName(employee.getName()).getInitials());
    }

    @Given("there is an employee named {string} in company with initials {string}")
    public void there_is_an_employee_named_in_company_with_initials(String s, String s2) {
        this.employee = new Employee(s);
        company.hireEmployee(employee);
        company.setInitailsForEmployee(employee,s2);
    }

    @Given("there is an employee named {string} not in company")
    public void there_is_an_employee_named_not_in_company(String s) {
        this.employee = new Employee(s);
    }

}
