Feature: Find a match
	Description: A user finds a match

Scenario: Match Found
	Given a marriage agency with a customer of age 30
	When a match for a customer with age 25 is searched
	Then the customer with age 30 is found
	
Scenario: No Match Found
	Given a marriage agency with a customer of age 30
	When a match for a customer with age 41 is searched
	Then no matches are found

