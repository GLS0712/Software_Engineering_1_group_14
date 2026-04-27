Feature: Employee

  Scenario: Employee seach found
    Given there is an employee named "John doe" in company
    When "John doe" is seached for
    Then "John doe" is found

  Scenario: Employee seach not found
    Given there is an employee named "John doe" not in company
    When "John doe" is seached for
    Then "John doe" is not found

    Scenario: Employee logs in
        Given there is an employee named "John doe" in company with initials "jodo"
        When "John doe" logs in with initals "jodo"
        Then "John doe" is logged in
Scenario: Employee logs in fail
        Given there is an employee named "John doe" in company with initials "jodo"
        When "John doe" logs in with initals "judo"
        Then the error message is "Employee not recognized"
