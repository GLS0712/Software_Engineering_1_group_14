Feature: Employee calendar

  Scenario: Employee gets a personal calendar
    Given the company exists
    When an employee "John doe" is hired
    Then "John doe" gets an personal calendar


#!SECTION registration of activities or time off
  Scenario: Register activity in employee calendar
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    When "John doe" registers activity "Design meeting" on "2026-05-01"
    Then "John doe" has 1 calendar entries on "2026-05-01"
    And the first calendar entry for "John doe" on "2026-05-01" has type "ACTIVITY"
    And the first calendar entry for "John doe" on "2026-05-01" has description "Design meeting"

  Scenario: Register time off in employee calendar
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    When "John doe" registers time off "SICK" on "2026-05-02"
    Then "John doe" has 1 calendar entries on "2026-05-02"
    And the first calendar entry for "John doe" on "2026-05-02" has type "TIME_OFF"
    And the first calendar entry for "John doe" on "2026-05-02" has description "SICK"

  Scenario: Date without registrations has no entries
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    Then "John doe" has 0 calendar entries on "2026-05-10"

  Scenario: Register multible activities in employee calendar
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    When "John doe" registers activity "Design meeting" and "Fixing code" on "2026-05-10"
    Then "John doe" has 2 calendar entries on "2026-05-10"

  Scenario: Register activity in employee calendar over long periode
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    When "John doe" registers activity "Fixing code" from "2026-05-10" to "2026-05-16"
    Then "John doe" has 1 calendar entries from "2026-05-10" to "2026-05-16"

  Scenario: Register activity on a day with an already registered activity
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    And "John doe" has an activity on "2026-05-10"
    When "John doe" registers activity "Fixing code" on "2026-05-10"
    Then "John doe" has 2 calendar entries on "2026-05-10"

  Scenario: Register activity on a day with 10 already registered activities and accepts the registration
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    And "John doe" has 10 calendar entries on "2026-05-08" already
    When "John doe" registers activity "Fixing code" on "2026-05-08"
    Then the message "are you sure this activity should be added, schedule is full"
    When "John doe" accepts
    Then then "John doe" registers activity "Fixing code" on "2026-05-08"

  Scenario: Register activity on a day with 10 already registered activities and does not accepts the registration
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    And "John doe" has 10 calendar entries on "2026-05-09" already
    When "John doe" registers activity "Fixing code" on "2026-05-09"
    Then the message "are you sure this activity should be added, schedule is full"
    When "John doe" does not accept
    Then "John doe" does not register activity "Fixing code" on "2026-05-09"

  Scenario: Get total entries over a date range
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    When "John doe" registers activity "Fixing code" from "2026-05-10" to "2026-05-16"
    Then "John doe" has 2 total calendar entries from "2026-05-10" to "2026-05-16"

  Scenario: Force register activity on a date outside the pre-initialised range
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    And "John doe" has 1 calendar entries on "2026-04-01" already
    Then "John doe" has 1 calendar entries on "2026-04-01"

  Scenario: Register activity over a date range outside the pre-initialised range
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    When "John doe" registers activity "Vacation" from "2026-04-01" to "2026-04-05"
    Then "John doe" has 1 total calendar entries from "2026-04-01" to "2026-04-05"


#!SECTION edit or removal of existing entries
  Scenario: remove activity
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    And "John doe" has 1 calendar entries on "2026-05-10" already
    When "John doe" removes a calendar entry on "2026-05-10"
    Then "John doe" has 0 calendar entries on "2026-05-10"

  Scenario: remove all activities
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    And "John doe" has 3 calendar entries on "2026-05-10" already
    When "John doe" removes all calendar entries on "2026-05-10"
    Then "John doe" has 0 calendar entries on "2026-05-10"

  Scenario: remove specific activity
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    And "John doe" registers activity "Design meeting" on "2026-05-10"
    And "John doe" registers activity "Fixing code" on "2026-05-10"
    When "John doe" removes activity "Design meeting" on "2026-05-10"
    Then "John doe" has 1 calendar entries on "2026-05-10"
    And the first calendar entry for "John doe" on "2026-05-10" has description "Fixing code"

  Scenario: remove activities over periode
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    And "John doe" registers activity "Fixing code" from "2026-05-10" to "2026-05-16"
    When "John doe" removes activity "Fixing code" from "2026-05-10" to "2026-05-16"
    Then "John doe" has 0 total calendar entries from "2026-05-10" to "2026-05-16"

  Scenario: remove all activities over periode
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    And "John doe" registers activity "Fixing code" from "2026-05-10" to "2026-05-16"
    And "John doe" registers activity "Design meeting" from "2026-05-10" to "2026-05-16"
    When "John doe" removes all activities from "2026-05-10" to "2026-05-16"
    Then "John doe" has 0 total calendar entries from "2026-05-10" to "2026-05-16"

  Scenario: change specifik activity
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    And "John doe" registers activity "Design meeting" on "2026-05-10"
    When "John doe" changes activity "Design meeting" to "Stand-up meeting" on "2026-05-10"
    Then the first calendar entry for "John doe" on "2026-05-10" has description "Stand-up meeting"

  Scenario: remove calendar entry on a date not in the calendar does nothing
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    When "John doe" removes a calendar entry on "2025-01-01"
    Then "John doe" has 0 calendar entries on "2025-01-01"

  Scenario: remove all calendar entries on a date not in the calendar does nothing
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    When "John doe" removes all calendar entries on "2025-01-01"
    Then "John doe" has 0 calendar entries on "2025-01-01"

  Scenario: remove specific activity on a date not in the calendar does nothing
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    When "John doe" removes activity "Design meeting" on "2025-01-01"
    Then "John doe" has 0 calendar entries on "2025-01-01"

  Scenario: change activity on a date not in the calendar does nothing
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    When "John doe" changes activity "Design meeting" to "Stand-up meeting" on "2025-01-01"
    Then "John doe" has 0 calendar entries on "2025-01-01"

  Scenario: change activity that does not exist on that date does nothing
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    And "John doe" registers activity "Design meeting" on "2026-05-10"
    When "John doe" changes activity "NonExistent" to "Stand-up meeting" on "2026-05-10"
    Then the first calendar entry for "John doe" on "2026-05-10" has description "Design meeting"


#!SECTION multi-employee calendar access
  Scenario: Employee can view another employee's calendar
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    And an employee "Jane smith" is hired
    And "Jane smith" gets an personal calendar
    And "John doe" registers activity "Design meeting" on "2026-05-10"
    When "Jane smith" views the calendar of "John doe" on "2026-05-10"
    Then "Jane smith" sees 1 calendar entries on the calendar of "John doe" on "2026-05-10"

  Scenario: Employee cannot register activity on another employee's calendar
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    And an employee "Jane smith" is hired
    And "Jane smith" gets an personal calendar
    When "Jane smith" tries to register activity "Design meeting" on "2026-05-10" in the calendar of "John doe"
    Then the message "You cannot modify another employee's calendar"
    And "John doe" has 0 calendar entries on "2026-05-10"

  Scenario: Employee cannot remove an activity from another employee's calendar
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    And an employee "Jane smith" is hired
    And "Jane smith" gets an personal calendar
    And "John doe" registers activity "Design meeting" on "2026-05-10"
    When "Jane smith" tries to remove activity "Design meeting" on "2026-05-10" from the calendar of "John doe"
    Then the message "You cannot modify another employee's calendar"
    And "John doe" has 1 calendar entries on "2026-05-10"

  Scenario: Employee cannot change an activity on another employee's calendar
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    And an employee "Jane smith" is hired
    And "Jane smith" gets an personal calendar
    And "John doe" registers activity "Design meeting" on "2026-05-10"
    When "Jane smith" tries to change activity "Design meeting" to "Stand-up" on "2026-05-10" in the calendar of "John doe"
    Then the message "You cannot modify another employee's calendar"
    And the first calendar entry for "John doe" on "2026-05-10" has description "Design meeting"
