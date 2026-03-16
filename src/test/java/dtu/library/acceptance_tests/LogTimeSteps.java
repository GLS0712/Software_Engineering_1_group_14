package dtu.library.acceptance_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LogTimeSteps {
    
@Given("An {string} has finished an activity")
public void anHasFinishedAnActivity(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
}


@When("the {string} logs that they have spend {int} hours")
public void theLogsThatTheyHaveSpendHours(String string, Integer int1) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
}


@Then("it should be logged that {int} hours have been spend")
public void itShouldBeLoggedThatHoursHaveBeenSpend(Integer int1) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
}
	
    
}