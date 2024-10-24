Feature: Join Game

  Background:
    And previous test data cleared
    And qa-user on the game-list page
    And an already existing game
    And qa-user name was not changed

  Scenario: Displays an error message when an error occurred on the backend
    Given an exception is thrown when calling joinGame
    And error logs are expected in the console
    When qa-user clicks on the join button
    And qa-user clicks on the join-game.validation button
    Then qa-user can see the join game dialog
    And qa-user can see the join-game.backend-error-message

  Rule: Default input values are correct
    Scenario: Without renaming user
      When qa-user clicks on the join button
      Then qa-user can see the join game dialog
      And the displayed value for player name is "qa-user"
    Scenario: After renaming user
      When qa-user changes his name to "other name"
      When qa-user clicks on the join button
      And the displayed value for player name is "other name"

  Rule: Can join a game
    Scenario: Can join a game without changing the player name
      When qa-user clicks on the join button
      And qa-user clicks on the join-game.validation button
      Then qa-user sees the following games, filtered for creators "test-user-1"
        | name        | creator     | players             | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-1 | test-user-1 | test-user-1,qa-user | CREATED | false         | null         | already joined | false        | false          |
    Scenario: Can join a game with another name
      When qa-user clicks on the join button
      And qa-user enters "other name" in the join-game.player-name field
      And qa-user clicks on the join-game.validation button
      Then qa-user sees the following games, filtered for creators "test-user-1"
        | name        | creator     | players                | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-1 | test-user-1 | test-user-1,other name | CREATED | false         | null         | already joined | false        | false          |

  Rule: Reset input field
    Background: qa-user has already entered another name
      Given qa-user clicks on the join button
      And qa-user enters "other name" in the join-game.player-name field
    Scenario: clicking on the reset button reset the player name to the user name
      When qa-user clicks on the join-game.reset button
      Then the displayed value for player name is "qa-user"
    Scenario: clicking on the validation button resets the player name to the user name
      When qa-user clicks on the join-game.validation button
      Then qa-user cannot see the join game dialog
      When qa-user clicks on the join button for the other game
      Then the displayed value for player name is "qa-user"

  Rule: Save input field
    Background: qa-user has already entered another name
      Given qa-user clicks on the join button
      And qa-user enters "other name" in the join-game.player-name field
    Scenario: clicking on the cancel button saves the input field
      When qa-user clicks on the join-game.cancel button
      And qa-user clicks on the join button
      Then the displayed value for player name is "other name"
    Scenario: closing the dialog by clicking outside of it saves the input field
      When qa-user clicks outside the join dialog
      And qa-user clicks on the join button
      Then the displayed value for player name is "other name"
    Scenario: closing the dialog by pressing escape saves the input field
      When qa-user presses the escape key on the join dialog
      And qa-user clicks on the join button
      Then the displayed value for player name is "other name"

  Rule: Form Validation
    Background:
      Given qa-user clicks on the join button
    Scenario Outline: Cannot create a game with a blank name
      When qa-user enters <name> in the join-game.player-name field
      Then the join-game.validate button is disabled
      Examples:
        | name              |
        | ""                |
        | " "               |
        | "  "              |
        | "[TAB]"           |
        | "[NEWLINE]"       |
        | "[NEWLINE] [TAB]" |
