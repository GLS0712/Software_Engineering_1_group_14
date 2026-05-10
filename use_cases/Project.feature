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

  Scenario: set EndDate
    Given there is an employee named "John doe"
    When "John doe" creates project with name "Make McDonalds ui" with end date "2026-12-01" and project leader "John doe"
    And "John doe" sets the project end date to "2026-11-15"
    Then the projects end date is "2026-11-15"

  Scenario: Remove project leader from project
    Given there is an employee named "John doe"
    When "John doe" creates project with name "Make McDonalds ui" with end date "2026-12-01" and project leader "John doe"
    And the project leader is removed
    Then the project named "Make McDonalds ui" has no projectLeader