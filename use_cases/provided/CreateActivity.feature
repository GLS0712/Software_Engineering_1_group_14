Feature: Create new activity
    Description: 

Scenario: Create activity
    Given there is an employee in the project
    When the employee creates a new activity with name: "New activity"
    Then there is a activity in the project called "New activity"

Scenario: Create project with the same name
    Given there is an employee
    And there is a project called "New activity"
    When employee tries to create project called "New activity"
    Then the error: "Already a project with given name" is called