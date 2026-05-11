Feature: save and load project/activities



#!section save project/activities
  @saves
  Scenario: saving a project writes it to persistent storage
    Given a project "TestProject" with id "P99001" is in memory
    When the project "TestProject" is saved to persistent storage
    Then persistent storage contains a project with id "P99001"

  @saves
  Scenario: saving a project with an activity persists the activity
    Given a project "TestProject" with id "P99001" is in memory
    And the project "TestProject" has an activity named "TestActivity"
    When the activity "TestActivity" of project "TestProject" is saved to persistent storage
    Then persistent storage contains an activity "TestActivity" for project "P99001"

#!section load project/activities
  @saves
  Scenario: loading from persistent storage restores a project
    Given the project "TestProject" with id "P99001" is only in persistent storage
    When projects are loaded from persistent storage
    Then the project "TestProject" is available in memory

  @saves
  Scenario: loading from persistent storage restores a project with its activities
    Given the project "TestProject" with id "P99001" and activity "TestActivity" are only in persistent storage
    When projects are loaded from persistent storage
    Then the project "TestProject" is available in memory
    And the project "TestProject" has the activity "TestActivity" in memory

  @saves
  Scenario: loading does not duplicate a project already in memory
    Given a project "TestProject" with id "P99001" is in memory
    And the project "TestProject" is saved to persistent storage
    And the project "TestProject" is still in memory
    When projects are loaded from persistent storage
    Then there is exactly one project named "TestProject" in memory

#!section delete project/activities
  @saves
  Scenario: deleting a project removes it from persistent storage
    Given a project "TestProject" with id "P99001" is in memory and in persistent storage
    When the project "TestProject" is deleted
    Then persistent storage does not contain a project with id "P99001"

  @saves
  Scenario: deleting an activity removes it from persistent storage
    Given a project "TestProject" with id "P99001" and activity "TestActivity" are in memory and in persistent storage
    When the activity "TestActivity" is deleted from project "TestProject"
    Then persistent storage does not contain an activity "TestActivity" for project "P99001"
