Feature: is an employee available
    Description: handles an employees availability when assigned multible activities

Scenario: is added to an activity when available
Given An "employee" is added to an activity
And the "employee" has 0 active activities
When the "employee" gets assigned
Then the "employee" is added to the activity

Scenario: is added to an activity when not available
Given An "employee" is added to an activity
And the "employee" has 10 active activities
When the "employee" gets assigned
Then the "employee" is not added to the activity