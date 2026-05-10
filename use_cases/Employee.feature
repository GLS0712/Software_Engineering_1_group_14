Feature: Employee

  Scenario: Employee seach found by name
    Given there is an employee named "John doe" in company
    When "John doe" is seached for
    Then "John doe" is found

  Scenario: Employee seach not found found by name
    Given there is an employee named "John doe" not in company
    When "John doe" is seached for
    Then "John doe" is not found

  Scenario: Employee seach found by initials
    Given there is an employee named "John doe" with initals "jodo" in company
    When initials "jodo" is seached for
    Then "John doe" is found

  Scenario: Employee seach not found found by name
    Given there is an employee named "John doe" not in company
    When initials "jodo" is seached for
    Then "John doe" is not found

  Scenario: Employee logs in
    Given there is an employee named "John doe" in company with initials "jodo"
    When "John doe" logs in with initals "jodo"
    Then "John doe" is logged in

  Scenario: Employee logs in fail
    Given there is an employee named "John doe" in company with initials "jodo"
    When "John doe" logs in with initals "judo"
    Then the error message is "Employee not recognized"
