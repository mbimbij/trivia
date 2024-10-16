Feature: Game Creation

  Background:
    And previous test data cleared
    And qa-user on the game-list page
    And qa-user name was not changed

  Scenario: Displays an error message when an error occurred on the backend
    Given an exception is thrown when calling createGame
    And error logs are expected in the console
    When qa-user clicks on create game button
    And qa-user enters the game name "newGame"
    And qa-user clicks on the create-game.validation button
    Then qa-user can see the create game dialog
    And qa-user can see the create-game.backend-error-message

  Rule: Default input values are correct
    Scenario: Without renaming user
      When qa-user clicks on create game button
      Then qa-user can see the create game dialog
      And the displayed value for game name is ""
      And the displayed value for creator name is "qa-user"
    Scenario: After renaming user
      When qa-user changes his name to "other name"
      And qa-user clicks on create game button
      And the displayed value for creator name is "other name"

  Rule: Create and display - happy case
    Scenario: actions on a game created without changing player's name
      When qa-user creates a game named "newGame" from the frontend
      Then qa-user sees the following games, filtered for creators "qa-user"
        | name    | creator | players | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | newGame | qa-user | qa-user | CREATED | false         | null         | already joined | false        | true           |
    Scenario: actions on a game created using a different name
      When qa-user creates a game named "newGame", with username "other-name", from the frontend
      Then qa-user sees the following games, filtered for creators "other-name"
        | name    | creator    | players    | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | newGame | other-name | other-name | CREATED | false         | null         | already joined | false        | true           |

  Rule: Preserve input values
    Background:
      Given qa-user clicks on create game button
      And qa-user enters the game name "newGame"
      And qa-user enters the creator name "some creator name"
    Scenario: closing the dialog by clicking on cancel button preserves input values
      When qa-user clicks on cancel button
      And qa-user clicks on create game button
      Then the displayed value for game name is "newGame"
      And the displayed value for creator name is "some creator name"
    Scenario: closing the dialog by clicking outside the dialog preserves input values
      When qa-user clicks outside the dialog
      And qa-user clicks on create game button
      Then the displayed value for game name is "newGame"
      And the displayed value for creator name is "some creator name"
    Scenario: closing the dialog by pressing escape preserves input values
      When qa-user presses the escape key on the create dialog
      And qa-user clicks on create game button
      Then the displayed value for game name is "newGame"
      And the displayed value for creator name is "some creator name"

  Rule: Reset input values
    Background:
      Given qa-user clicks on create game button
      And qa-user enters the game name "newGame"
      And qa-user enters the creator name "some creator name"
    Scenario: click on reset button clears the inputs but does not close the dialog
      When qa-user clicks on reset button
      Then qa-user can see the create game dialog
      Then the displayed value for game name is ""
      And the displayed value for creator name is "qa-user"
    Scenario: creating a game closes the dialog and resets the dialog inputs
      When qa-user clicks on the create-game.validation button and saves the game id
      Then qa-user cannot see the create game dialog
      When qa-user clicks on create game button
      Then the displayed value for game name is ""
      And the displayed value for creator name is "qa-user"
    Scenario: Renaming the user reset the creator name field but preserves the game name
      When qa-user presses the escape key on the create dialog
      When qa-user changes his name to "other name" without navigation
      Given qa-user clicks on create game button
      Then the displayed value for game name is "newGame"
      And the displayed value for creator name is "other name"

  Rule: Form Validation
    Background:
      Given qa-user clicks on create game button
    Scenario Outline: Cannot create a game with a blank name
      When qa-user enters the game name <name>
      Then the validate button is disabled
      Examples:
        | name              |
        | ""                |
        | " "               |
        | "  "              |
        | "[TAB]"           |
        | "[NEWLINE]"       |
        | "[NEWLINE] [TAB]" |
    Scenario Outline: Cannot create a game with a blank creator name
      When qa-user enters the game name "some name"
      But qa-user enters the creator name <name>
      Then the validate button is disabled
      Examples:
        | name              |
        | ""                |
        | " "               |
        | "  "              |
        | "[TAB]"           |
        | "[NEWLINE]"       |
        | "[NEWLINE] [TAB]" |
