Feature: Employee intials

  Scenario: Set initals for employee
    Given there is an employee named "John doe" in company
    And "John doe" does not have any intials
    When "John doe" sets initials as "jodo"
    Then his initials are "jodo"

  Scenario: set intials when already exists
    Given there is an employee named "John doe" in company
    And "John doe" does not have any intials
    And there is an employee named "John doe" in company with initials "jodo"
    When "John doe" sets initials as "jodo"
    Then the error message is "Initials already exists"

  Scenario: set intials when not in company
    Given there is an employee named "John Smith" not in company
    And "John Smith" does not have any intials
    When "John Smith" sets initials as "josm"
    Then the error message is "Employee not part of company"
