Feature: Employee calendar

  Scenario: Employee gets a personal calendar
    Given the company exists
    When an employee "John doe" is hired
    Then "John doe" gets an personal calendar
