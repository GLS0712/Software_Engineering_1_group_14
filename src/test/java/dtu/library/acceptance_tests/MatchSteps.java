package dtu.library.acceptance_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import dtu.library.app.Customer;
import dtu.library.app.MarriageAgency;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MatchSteps {

	private MarriageAgency marriageAgency;
	private Customer customer;

	/*
	 * Note that the constructor is apparently never called, but there are no null
	 * pointer exceptions regarding that libraryApp is not set. When creating the
	 * BookSteps object, the Cucumber libraries are using that constructor with an
	 * object of class LibraryApp as the default.
	 * 
	 * This also holds for all other step classes that have a similar constructor.
	 * In this case, the <b>same</b> object of class LibraryApp is used as an
	 * argument. This provides an easy way of sharing the same object, in this case
	 * the object of class LibraryApp and the errorMessage Holder, among all step classes.
	 * 
	 * This principle is called <em>dependency injection</em>. More information can
	 * be found in the "Cucumber for Java" book available online from the DTU Library.
	 */
	public MatchSteps() {
		this.marriageAgency = new MarriageAgency();
	}

    @Given("a marriage agency with a customer of age {int}")
    public void a_marriage_agency_with_a_customer_of_age(int i) {
        assertTrue(marriageAgency.customerExist(i));
    }

    @When("a match for a customer with age {int} is searched")
	public void a_match_for_a_customer_with_age_is_searched(int i) {
        customer = marriageAgency.currentCustomer(i);
    }

    @Then("the customer with age {int} is found")
    public void the_customer_with_age_is_found(int i) {
        assertTrue(marriageAgency.findMatch(customer));
		System.out.println(customer.getAge());
		System.out.println(marriageAgency.findMatch(customer));
    }

    @Then("no matches are found")
    public void no_matches_are_found() {
		assertTrue(marriageAgency.findMatch(customer));
		System.out.println(customer.getAge());
		System.out.println(marriageAgency.findMatch(customer));
    }	
    
}