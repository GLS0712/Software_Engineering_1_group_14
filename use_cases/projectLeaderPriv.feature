Feature: Project Leader Privelidges

    Scenario: A project leader is assigned
        Given there is a project
        And there is no project leader on project
        When An Employee Assigns a project leader "LeaderInitials"
        Then the projectleader is "LeaderInitials"
    
    Scenario: A project leader is already assigned
        Given there is a project
        And there is project leader on project
        When An Employee Assigns a project leader "LeaderInitials"
        Then the projectleader is not "LeaderInitials"
        And the error code is "leader already assigned" 

  