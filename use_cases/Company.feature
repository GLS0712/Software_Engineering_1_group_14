Feature: Create project

Scenario: create project with name and time
Given there is an employee named "John doe"
When employee creates project with "Name" and "Time"
Then there is a project named "Name" with "Time"


Scenario: create project with only name
Given there is an employee named "John doe"
When employee creates project with "Name"
Then there is a project named "Name"


Scenario: create project with name, time and projectLeader
Given there is an employee named "John doe"
When John doe creates project with "Name", "time" and "John doe" as projectLeader
Then there is a project named "Name" with "time" and "John doe" is projectLeader