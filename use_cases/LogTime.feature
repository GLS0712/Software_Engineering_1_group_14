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

    #Illegal Argument Error handeling
    Scenario: Logging negative hours is not allowed
        Given employee "fuba" is assigned to activity "Activity_1" in project "26001"
        When "fuba" tries to log -2.0 hours on "Activity_1"
        Then the system should reject the entry with error: "Hours must be positive"
        And no time should be registered for "fuba" on "Activity_1"

    Scenario: Logging more than 24 hours in a day is not allowed
        Given employee "fuba" is assigned to activity "Activity_1" in project "26001"
        When "fuba" tries to log 25.0 hours on "Activity_1"
        Then the system should reject the entry with error: "Hours cannot exceed 24"

    Scenario: Hours must be in half-hour increments
        Given employee "fuba" is assigned to activity "Activity_1" in project "26001"
        When "fuba" tries to log 2.3 hours on "Activity_1"
        Then the system should reject the entry with error: "Hours must be in half-hour increments"

    Scenario: Cannot log time on a future date
        Given employee "fuba" is assigned to activity "Activity_1" in project "26001"
        And today is "2026-05-08"
        When "fuba" tries to log 2.0 hours on "Activity_1" on "2026-05-15"
        Then the system should reject the entry with error: "Cannot log time on a future date"
        And no time should be registered for "fuba"

    Scenario: Logging time on a past date is allowed
        Given employee "huba" is assigned to activity "Activity_1" in project "26001"
        And today is "2026-05-08"
        When "huba" logs 2.0 hours on "Activity_1" on "2026-05-04"
        Then 2.0 hours should be registered on "Activity_1" for "huba"

    Scenario: Logging zero hours is not allowed
        Given employee "fuba" is assigned to activity "Activity_1" in project "26001"
        When "fuba" tries to log 0.0 hours on "Activity_1"
        Then the system should reject the entry with error: "Hours must be positive"

    Scenario: Logging exactly 24 hours is allowed
        Given employee "huba" is assigned to activity "Activity_1" in project "26001"
        When "huba" logs 24.0 hours on "Activity_1"
        Then 24.0 hours should be registered on "Activity_1" for "huba"

    Scenario: Logging exactly half an hour is allowed
        Given employee "huba" is assigned to activity "Activity_1" in project "26001"
        When "huba" logs 0.5 hours on "Activity_1"
        Then 0.5 hours should be registered on "Activity_1" for "huba"

    Scenario: Logging time on today's date is allowed
        Given employee "huba" is assigned to activity "Activity_1" in project "26001"
        And today is "2026-05-08"
        When "huba" logs 2.0 hours on "Activity_1" on "2026-05-08"
        Then 2.0 hours should be registered on "Activity_1" for "huba"

    Scenario: Logging multiple entries on the same activity registers all of them
        Given employee "huba" is assigned to activity "Activity_1" in project "26001"
        When "huba" logs 2.0 hours on "Activity_1"
        And "huba" logs 3.0 hours on "Activity_1"
        Then 2 log entries should exist for "huba" on "Activity_1"

    Scenario: Different employees can log time on the same activity
        Given employee "huba" is assigned to activity "Activity_1" in project "26001"
        And employee "fuba" is not assigned to "Activity_1"
        When "huba" logs 2.0 hours on "Activity_1"
        And "fuba" logs 3.0 hours on "Activity_1"
        Then 1 log entries should exist for "huba" on "Activity_1"
        And 1 log entries should exist for "fuba" on "Activity_1"

    Scenario: Editing an entry does not create a duplicate
        Given "huba" has logged 2.0 hours on "Activity_1" in "26001"
        When "huba" updates the entry to 3.0 hours
        Then 1 log entries should exist for "huba" on "Activity_1"

    Scenario: Updating an entry preserves the entry ID
        Given "huba" has logged 2.0 hours on "Activity_1" in "26001"
        When "huba" updates the entry to 3.0 hours
        Then the entry ID should remain unchanged

    Scenario: Updating an entry changes the hours without adding a duplicate
        Given "huba" has logged 2.0 hours on "Activity_1" in "26001"
        When "huba" updates the entry to 4.0 hours
        Then 1 log entries should exist for "huba" on "Activity_1"
        And 4.0 hours should now be registered on "Activity_1" for "huba"

    Scenario: Updating a non-existent entry leaves the log unchanged
        Given "huba" has logged 2.0 hours on "Activity_1" in "26001"
        When the system tries to update entry "999" to 5.0 hours
        Then 1 log entries should exist for "huba" on "Activity_1"
        And 2.0 hours should be registered on "Activity_1" for "huba"

    Scenario: Deleting an entry removes it from the log
        Given "huba" has logged 2.0 hours on "Activity_1" in "26001"
        When "huba" deletes the entry
        Then 0 log entries should exist for "huba" on "Activity_1"

    Scenario: Deleting an entry with multiple entries removes only that one
        Given "huba" has logged 2.0 hours on "Activity_1" in "26001"
        When "huba" logs 3.0 hours on "Activity_1"
        And "huba" deletes the entry
        Then 1 log entries should exist for "huba" on "Activity_1"

    Scenario: Deleting all entries for an activity leaves the log empty for that activity
        Given "huba" has logged 2.0 hours on "Activity_1" in "26001"
        When "huba" logs 3.0 hours on "Activity_1"
        And "huba" deletes the entry
        And "huba" deletes the entry
        Then 0 log entries should exist for "huba" on "Activity_1"
