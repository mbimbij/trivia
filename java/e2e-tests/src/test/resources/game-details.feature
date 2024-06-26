Feature: Game Details Page

  Background:
    Given previous test data cleared
    And 2 existing games
    And a logged-in test user on the game-list page

  Scenario Outline: Verify Game Details Displayed - On an existing game
    When i click on game details link for "<game>"
    Then i am on the on game details page for "<game>"
    And the following games are displayed
      | name   | creator   | players   | state   | start_enabled | join_enabled | join_text | goto_enabled |
      | <game> | <creator> | <creator> | created | <se1>         | <je1>        | <jt1>     | <ge1>        |
    And no error is displayed in the console
    When "<newcomer>" joins "<game>"
    Then the following games are displayed
      | name   | creator   | players              | state   | start_enabled | join_enabled | join_text | goto_enabled |
      | <game> | <creator> | <creator>,<newcomer> | created | <se2>         | <je2>        | <jt2>     | <ge2>        |
    When "<creator>" starts "<game>"
    Then the following games are displayed
      | name   | creator   | players              | state   | start_enabled | join_enabled | join_text | goto_enabled |
      | <game> | <creator> | <creator>,<newcomer> | started | <se3>         | <je3>        | <jt3>     | <ge3>        |
    Examples:
      | game        | creator     | newcomer    | se1  | je1  | jt1            | ge1   | se2  | je2  | jt2            | ge2   | se3  | je3  | jt3          | ge3   |
    # (I) joins an (existing) game created by (other)
      | test-game-1 | test-user-1 | qa-user     | null | true | join           | false | null | null | already joined | false | null | null | game started | true  |
    # (other) joins an (existing) game created by (me)
      | test-game-2 | qa-user     | test-user-1 | null | null | already joined | false | true | null | already joined | false | null | null | game started | true  |
    # (other) joins an (existing) game created by (other)
      | test-game-1 | test-user-1 | test-user-2 | null | true | join           | false | null | true | join           | false | null | null | game started | false |

  Scenario Outline: Verify Game Details Displayed - On a newly created game
    When <creator> creates a game named "<game>"
    When i click on game details link for "<game>"
    Then i am on the on game details page for "<game>"
    And the following games are displayed
      | name   | creator   | players   | state   | start_enabled | join_enabled | join_text | goto_enabled |
      | <game> | <creator> | <creator> | created | <se1>         | <je1>        | <jt1>     | <ge1>        |
    And no error is displayed in the console
    When "<newcomer>" joins "<game>"
    Then the following games are displayed
      | name   | creator   | players              | state   | start_enabled | join_enabled | join_text | goto_enabled |
      | <game> | <creator> | <creator>,<newcomer> | created | <se2>         | <je2>        | <jt2>     | <ge2>        |
    When "<creator>" starts "<game>"
    Then the following games are displayed
      | name   | creator   | players              | state   | start_enabled | join_enabled | join_text | goto_enabled |
      | <game> | <creator> | <creator>,<newcomer> | started | <se3>         | <je3>        | <jt3>     | <ge3>        |
    Examples:
      | game    | creator     | newcomer    | se1  | je1  | jt1            | ge1   | se2  | je2  | jt2            | ge2   | se3  | je3  | jt3          | ge3   |
    # (I) joins an (new) game created by (other)
      | newGame | test-user-1 | qa-user     | null | true | join           | false | null | null | already joined | false | null | null | game started | true  |
#    # (other) joins an (new) game created by (me)
      | newGame | qa-user     | test-user-1 | null | null | already joined | false | true | null | already joined | false | null | null | game started | true  |
#    # (other) joins an (new) game created by (other)
      | newGame | test-user-1 | test-user-2 | null | true | join           | false | null | true | join           | false | null | null | game started | false |


  Scenario Outline: Verify Game Details Displayed - Direct url access and refresh
    When i directly access the game-details page for "<game>"
    And qa-user refresh
    Then i am on the on game details page for "<game>"
    And the following games are displayed
      | name   | creator   | players   | state   | start_enabled | join_enabled | join_text | goto_enabled |
      | <game> | <creator> | <creator> | created | <se1>         | <je1>        | <jt1>     | <ge1>        |
    And no error is displayed in the console
    When "<newcomer>" joins "<game>"
    Then the following games are displayed
      | name   | creator   | players              | state   | start_enabled | join_enabled | join_text | goto_enabled |
      | <game> | <creator> | <creator>,<newcomer> | created | <se2>         | <je2>        | <jt2>     | <ge2>        |
    When "<creator>" starts "<game>"
    Then the following games are displayed
      | name   | creator   | players              | state   | start_enabled | join_enabled | join_text | goto_enabled |
      | <game> | <creator> | <creator>,<newcomer> | started | <se3>         | <je3>        | <jt3>     | <ge3>        |
    Examples:
      | game        | creator     | newcomer    | se1  | je1  | jt1            | ge1   | se2  | je2  | jt2            | ge2   | se3  | je3  | jt3          | ge3   |
    # (I) joins an (existing) game created by (other)
      | test-game-1 | test-user-1 | qa-user     | null | true | join           | false | null | null | already joined | false | null | null | game started | true  |
    # (other) joins an (existing) game created by (me)
      | test-game-2 | qa-user     | test-user-1 | null | null | already joined | false | true | null | already joined | false | null | null | game started | true  |
    # (other) joins an (existing) game created by (other)
      | test-game-1 | test-user-1 | test-user-2 | null | true | join           | false | null | true | join           | false | null | null | game started | false |