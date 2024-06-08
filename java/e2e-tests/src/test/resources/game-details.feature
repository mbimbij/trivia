Feature: Game Details Page

  Background:
    Given previous test data cleared
    And 2 existing games
    And a logged-in test user on the game-list page

  Scenario Outline: Verify Game Details Displayed - Join an existing game created by other
    When "test-user-1" creates a game named "newGame"
    When i click on game details link for "<game>"
    When i am on the on game details page for "<game>"
    Then the following games are displayed
      | name   | creator     | players     | state   | start_enabled | join_enabled | join_text | goto_enabled | delete_enabled |
      | <game> | test-user-1 | test-user-1 | created | null          | true         | join      | false        | null           |
    And no error is displayed in the console
    # join-action: by(me), on(other), and(existing)
    When qa-user clicks on "join" button for "<game>"
    Then the following games are displayed
      | name   | creator     | players             | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
      | <game> | test-user-1 | test-user-1,qa-user | created | null          | null         | already joined | false        | null           |
    When test-user-1 starts <game>
    Then the following games are displayed
      | name   | creator     | players             | state   | start_enabled | join_enabled | join_text    | goto_enabled | delete_enabled |
      | <game> | test-user-1 | test-user-1,qa-user | started | null          | null         | game started | true         | null           |
    Examples:
      | game        |
      | test-game-1 |
