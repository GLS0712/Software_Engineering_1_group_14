Feature: project sets and gets

  Scenario: set name
    Given there is an employee named "John doe"
    When "John doe" creates project with name "Make McDonalds ui" with end date "2026-12-01" and project leader "John doe"
    And "John doe" set projects name to "new name"
    Then the projects name is "new name"

  Scenario: set Description
    Given there is an employee named "John doe"
    When "John doe" creates project with name "Make McDonalds ui" with end date "2026-12-01" and project leader "John doe"
    And "John doe" set projects Description to "This is a Description"
    Then the projects Description is "This is a Description"

 Scenario: set StartDate
    Given there is an employee named "John doe"
    When "John doe" creates project with name "Make McDonalds ui" with end date "2026-12-01" and project leader "John doe"
    And "John doe" set projects startDate to "2026-11-01"
    Then the projects startDate is "2026-11-01"