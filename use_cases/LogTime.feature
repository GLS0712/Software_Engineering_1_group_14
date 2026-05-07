Feature: time logging
    Description: An employe logs the time used on an activity

    Scenario: Employee logs hours on an assigned activity
        Given employee "huba" is assigned to activity "Activity_1" in project "26001"
        When "huba" logs 2.0 hours on "Activity_1"
        Then 2.0 hours should be registered on "Activity_1" for "huba"

    Scenario: Hours are recorded against the date they were worked
        Given employee "huba" is assigned to activity "Activity_1" in project "26001"
        When "huba" logs 2.0 hours on "Activity_1" on "1111-11-1"
        Then "huba"'s time sheet for "1111-11-11" shows 2.0 hours on "Activity_1"

    Scenario: Employee can log time on an activity they are not assigned to
        Given activity "Activity_1" exists in project "26001"
        And employee "huba" is not assigned to "Activity_1"
        When "huba" logs 2.0 hours on "Activity_1"
        Then 2.0 hours should be registered on "Activity_1" for "huba"

    Scenario: Logging half-hour increments
        Given employee "huba" is assigned to activity "Activity_1" in project "26001"
        When "huba" logs 1.5 hours on "Activity_1"
        Then 1.5 hours should be registered on "Activity_1" for "huba"

    Scenario: Employee corrects a previously logged entry
        Given "huba" has logged 2.0 hours on "Activity_1" in "26001"
        When "huba" updates the entry to 3.0 hours
        Then 3.0 hours should now be registered on "Activity_1" for "huba"
