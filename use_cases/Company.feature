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
        When "John doe" creates project with name "Make McDonalds ui" with end date "2026-12-01"
        And "John doe" is assigned as projectLeader
        Then there is a project named "Make McDonalds ui" with an end date "2026-12-01"
        And the project named "Make McDonalds ui" has the projectLeader "John doe"