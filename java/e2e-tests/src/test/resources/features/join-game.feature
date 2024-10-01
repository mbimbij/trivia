Feature: Join Game

  Background:
    And previous test data cleared
    And qa-user on the game-list page
    And an already existing game
    And qa-user name was not changed

  Rule: Default input values are correct
    Scenario: Without renaming user
      When qa-user clicks on the join button
      Then qa-user can see the join game dialog
      And the displayed value for player name is "qa-user"
    Scenario: After renaming user
      When qa-user changes his name to "other name"
      When qa-user clicks on the join button
      And the displayed value for player name is "other name"
