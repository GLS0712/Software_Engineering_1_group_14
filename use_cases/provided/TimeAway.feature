Feature: Sick leave or holiday availability

    Scenario: A person becomes Sick
        Given there is an Employee
        When the Employee reports sickness on date "06-06-2026"
        Then the employee is not available all of "06-06-2026"

    Scenario: An employee requests holiday 
        Given there is an Employee
        When the Employee reports holiday from "06-06-2026" to "08-06-2026"
        Then the employee is not available all of "06-06-2026"
        And the employee is not available all of "07-06-2026"
        And the employee is not available all of "08-06-2026"
    