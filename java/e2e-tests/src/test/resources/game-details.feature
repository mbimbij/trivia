Feature: Game Details Page

  Background:
    Given previous test data cleared
    And 2 existing games
    And a logged-in test user on the game-list page

  Scenario Outline: Verify Game Details Displayed - Join an existing game created by other
    When "<newGameCreator>" creates a game named "<newGameName>"
    When i click on game details link for "<game>"
    When i am on the on game details page for "<game>"
    Then the following games are displayed
      | name   | creator   | players   | state   | start_enabled | join_enabled | join_text | goto_enabled |
      | <game> | <creator> | <creator> | created | <se1>         | <je1>        | <jt1>     | <ge1>        |
    And no error is displayed in the console
    # join-action: by(me), on(other), and(existing)
    When "<newPlayer>" joins "<game>" bis
    Then the following games are displayed
      | name   | creator   | players               | state   | start_enabled | join_enabled | join_text | goto_enabled |
      | <game> | <creator> | <creator>,<newPlayer> | created | <se2>         | <je2>        | <jt2>     | <ge2>        |
    When <creator> starts <game>
    Then the following games are displayed
      | name   | creator   | players               | state   | start_enabled | join_enabled | join_text | goto_enabled |
      | <game> | <creator> | <creator>,<newPlayer> | started | <se3>         | <je3>        | <jt3>     | <ge3>        |
    Examples:
      | game        | creator     | newPlayer | newGameName | newGameCreator | se1  | je1  | jt1  | ge1   | se2  | je2  | jt2            | ge2   | se3  | je3  | jt3          | ge3  |
      | test-game-1 | test-user-1 | qa-user   | newGame     | test-user-1    | null | true | join | false | null | null | already joined | false | null | null | game started | true |
#      | test-game-2 | qa-user     | test-user-1 | newGame     | test-user-1    |                       |                      |                   |                      |                      |
