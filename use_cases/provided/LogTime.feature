Feature: time logging
    Description: An employe logs the time used on an activity

Scenario: 2 hours spend
    Given An "employe" has finished an activity
    When the "employe" logs that they have spend 2 hours
    Then it should be logged that 2 hours have been spend

Scenario: 5 hours spend
    Given An "employe" has finished an activity
    When the "employe" logs that they have spend 5 hours
    Then it should be logged that 5 hours have been spend
