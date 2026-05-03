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

  Scenario: get activity from name
    Given there is a project
    And an employee "John doe" is assigned to project
    And there is an activity with name "chungus"
    When an employee "John doe" searches for the activity "chungus"
    Then the activity "chungus" is found

  Scenario: get activity from name that doesn't exist
    Given there is a project
    And an employee "John doe" is assigned to project
    And there is not an activity with name "chungus"
    When an employee "John doe" searches for the activity "chungus"
    Then the activity "chungus" is Not found


#!SECTION adding employees to activity with availability check
  Scenario: Add employee to activity when available during the activity period
    Given there is a project
    And an employee "John doe" is assigned to project
    And the projectLeader is "John doe"
    And "John doe" has a personal calendar
    And there is an activity "Design meeting" from "2026-06-01" to "2026-06-05"
    When "John doe" is added to activity "Design meeting"
    Then "John doe" is assigned to activity "Design meeting"

  Scenario: Cannot add employee to activity when calendar is full during the activity period
    Given there is a project
    And an employee "John doe" is assigned to project
    And the projectLeader is "John doe"
    And "John doe" has a personal calendar
    And "John doe" has a full schedule on "2026-06-03"
    And there is an activity "Design meeting" from "2026-06-01" to "2026-06-05"
    When "John doe" is added to activity "Design meeting"
    Then the error message is "Employee is not available during the activity period"
    And "John doe" is not assigned to activity "Design meeting"

  Scenario: Employee with partial calendar can still be added to activity
    Given there is a project
    And an employee "John doe" is assigned to project
    And the projectLeader is "John doe"
    And "John doe" has a personal calendar
    And "John doe" has 5 existing entries on "2026-06-03"
    And there is an activity "Design meeting" from "2026-06-01" to "2026-06-05"
    When "John doe" is added to activity "Design meeting"
    Then "John doe" is assigned to activity "Design meeting"


#!SECTION if a project leader exist in a project only the projectleader can add employees
  Scenario: Project leader can add an employee to activity
    Given there is a project
    And an employee "John doe" is assigned to project
    And the projectLeader is "John doe"
    And an employee "Jane smith" is assigned to project
    And "Jane smith" has a personal calendar
    And there is an activity "Design meeting" from "2026-06-01" to "2026-06-05"
    When "John doe" adds "Jane smith" to activity "Design meeting"
    Then "Jane smith" is assigned to activity "Design meeting"

  Scenario: Non-project-leader cannot add an employee to activity
    Given there is a project
    And an employee "John doe" is assigned to project
    And the projectLeader is "John doe"
    And an employee "Jane smith" is assigned to project
    And "Jane smith" has a personal calendar
    And there is an activity "Design meeting" from "2026-06-01" to "2026-06-05"
    When "Jane smith" adds "Jane smith" to activity "Design meeting"
    Then the error message is "you are not projectLeader"
    And "Jane smith" is not assigned to activity "Design meeting"

  Scenario: Anyone can add employee to activity when there is no project leader
    Given there is a project
    And an employee "John doe" is assigned to project
    And there is no projectleader
    And an employee "Jane smith" is assigned to project
    And "Jane smith" has a personal calendar
    And there is an activity "Design meeting" from "2026-06-01" to "2026-06-05"
    When "John doe" adds "Jane smith" to activity "Design meeting"
    Then "Jane smith" is assigned to activity "Design meeting"
