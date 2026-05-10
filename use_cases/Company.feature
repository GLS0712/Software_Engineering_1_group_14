Feature: Create project

    Scenario: create project with name and no end date
        Given there is an employee named "John doe"
        When "John doe" creates project with name "Make McDonalds ui" without end date
        Then there is a project named "Make McDonalds ui" with no end date
        And the project named "Make McDonalds ui" has no projectLeader

    Scenario: create project with name and an end date
        Given there is an employee named "John doe"
        When "John doe" creates project with name "Make McDonalds ui" with end date "2026-12-01"
        Then there is a project named "Make McDonalds ui" with an end date "2026-12-01"
        And the project named "Make McDonalds ui" has no projectLeader

    Scenario: create project with name, end date and projectLeader
        Given there is an employee named "John doe"
        When "John doe" creates project with name "Make McDonalds ui" with end date "2026-12-01" and project leader "John doe"
        Then there is a project named "Make McDonalds ui" with an end date "2026-12-01"
        And the project named "Make McDonalds ui" has the projectLeader "John doe"


#!SECTION setting the id for a project
  Scenario: create project and set id
    Given there is an employee named "John doe"
    When "John doe" creates project with name "Make McDonalds ui" with end date "2026-12-01"
    And "John doe" sets the id for "Make McDonalds ui"
    Then there is a project named "Make McDonalds ui" with id "P26001"

 Scenario: create project and set id when already a project
    Given there is an employee named "John doe"
    And there is another project with an id
    When "John doe" creates project with name "Make McDonalds ui" with end date "2026-12-01"
    And "John doe" sets the id for "Make McDonalds ui"
    Then there is a project named "Make McDonalds ui" with id "P26002"

#!SECTION updating all activities endDate in project when the project endDate is reduced
  Scenario: Activity end date is capped when project end date is reduced below it
    Given there is a project with end date "2026-12-01"
    And an employee "John doe" is assigned to project
    And the projectLeader is "John doe"
    And there is an activity "Design meeting" from "2026-06-01" to "2026-11-30"
    When the project end date is reduced to "2026-10-01"
    Then the activity "Design meeting" has end date "2026-10-01"

  Scenario: Activity end date is unchanged when it is within the new project end date
    Given there is a project with end date "2026-12-01"
    And an employee "John doe" is assigned to project
    And the projectLeader is "John doe"
    And there is an activity "Design meeting" from "2026-06-01" to "2026-08-01"
    When the project end date is reduced to "2026-10-01"
    Then the activity "Design meeting" has end date "2026-08-01"

  Scenario: Only activities exceeding the new end date are capped
    Given there is a project with end date "2026-12-01"
    And an employee "John doe" is assigned to project
    And the projectLeader is "John doe"
    And there is an activity "Early meeting" from "2026-06-01" to "2026-08-01"
    And there is an activity "Late meeting" from "2026-09-01" to "2026-11-30"
    When the project end date is reduced to "2026-10-01"
    Then the activity "Early meeting" has end date "2026-08-01"
    And the activity "Late meeting" has end date "2026-10-01"