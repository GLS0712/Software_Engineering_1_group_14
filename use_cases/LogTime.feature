Feature: time logging
    Description: An employe logs the time used on an activity

Scenario: 2 hours spend
    Given An "employee" has finished an activity
    When the "employee" logs that they have spend 2 hours
    Then it should be logged that 2 hours have been spend

Scenario: 5 hours spend
    Given An "employee" has finished an activity
    When the "employee" logs that they have spend 5 hours
    Then it should be logged that 5 hours have been spend

Scenario: more timed logged than possible
    Given An "employee" has finished an activity with total time of 2 days
    When the "employee" logs that they have spend 17 hours
    Then no time should be logged
    And the "ProjectLeader" should be notified of incorrect time logged