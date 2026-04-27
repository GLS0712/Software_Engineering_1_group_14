Feature: Employee calendar

  Scenario: Employee gets a personal calendar
    Given the company exists
    When an employee "John doe" is hired
    Then "John doe" gets an personal calendar

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
    And "John doe" has 10 calendar entries on "2026-05-08"
    When "John doe" registers activity "Fixing code" on "2026-05-08"
    Then the message "are you sure this activity should be added, schedule is full"
    When "John doe" accepts
    Then then "John doe" registers activity "Fixing code" on "2026-05-08"

  Scenario: Register activity on a day with 10 already registered activities and does not accepts the registration
    Given the company exists
    And an employee "John doe" is hired
    And "John doe" gets an personal calendar
    And "John doe" has 10 calendar entries on "2026-05-09"
    When "John doe" registers activity "Fixing code" on "2026-05-09"
    Then the message "are you sure this activity should be added, schedule is full"
    When "John doe" does not accept
    Then "John doe" does not register activity "Fixing code" on "2026-05-09"