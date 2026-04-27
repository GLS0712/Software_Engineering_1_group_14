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
