Feature: is an employe available
    Description: handles employes availability when assigned multible activities

Scenario: is added to an activity when available
Given An employe is added to an activity
And the employe has 0 active activities
When the employe gets assigned
Then the employe is added to the activity

Scenario: is added to an activity when not available
Given An employe is added to an activity
And the employe has 10 active activities
When the employe gets assigned
Then the employe is not added to the activity