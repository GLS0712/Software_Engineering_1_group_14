package dtu.acceptance_tests;

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
    }

    @When("the {string} logs that they have spend {int} hours")
    public void theLogsThatTheyHaveSpendHours(String string, Integer int1) {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("it should be logged that {int} hours have been spend")
    public void itShouldBeLoggedThatHoursHaveBeenSpend(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("{string} should be notified of incorrect time logged")
    public void should_be_notified_of_incorrect_time_logged(String s) {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("no time should be logged")
    public void no_time_should_be_logged() {
        // Write code here that turns the phrase above into concrete actions
    }

    @Given("An {string} has finished an activity with total time of {int} days")
    public void An_has_finished_an_activity_with_total_time_of_day(String s, int i) {
        // Write code here that turns the phrase above into concrete actions
    }
	
}