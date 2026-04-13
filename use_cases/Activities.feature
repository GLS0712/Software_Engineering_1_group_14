Feature: activity

  Scenario: create activity with name and Description and no projectleader
    Given there is a project
    And an employee "John doe" is assigned to project
    And there is no projectleader
    When "John doe" creates activity with "name" and "description"
    Then there exists an activity with "name" and "description"

  Scenario: create activity with name and Description as projectleader
    Given there is a project
    And an employee "John doe" is assigned to project
    And the projectLeader is "John doe"
    When "John doe" creates activity with "name" and "description"
    Then there exists an activity with "name" and "description"

  Scenario: create activity with name and Description with different projectleader
    Given there is a project
    And an employee "John doe" is assigned to project
    And the projectLeader is not "John doe"
    When "John doe" creates activity with "name" and "description"
    Then the error message is "you are not projectLeader"
