Feature: Game Details Page

  Background:
    And previous test data cleared
    And 2 existing games
    And qa-user on the game-list page
    And qa-user name was not changed

  Rule: Verify Error display
    Scenario: Display error message when game not found
      And qa-user directly access the game-details page for game id = -1
      Then ok section is not visible
      And loading section is not visible
      And error section is visible
      And error section text is "Game with id -1 not found"
      And error logs are expected in the console

    Scenario: Display error message when backend exception
      Given an exception is thrown when calling getGameById
      And qa-user directly access the game-details page for game id = -1
      Then ok section is not visible
      And loading section is not visible
      And error section is visible
      And error section text contains "Error loading game with id"
      And error section text contains "message: some backend exception"
      And error logs are expected in the console

    Scenario Outline: Verify Game Details Displayed - On an existing game
      When qa-user clicks on game details link for "<game>"
      Then i am on the on game details page for "<game>"
      And the following games are displayed
        | name   | creator   | players   | state   | start_enabled | join_enabled | join_text | goto_enabled |
        | <game> | <creator> | <creator> | CREATED | <se1>         | <je1>        | <jt1>     | <ge1>        |
      When <newcomer> joins "<game>" <jBeFe>
      Then the following games are displayed
        | name   | creator   | players              | state   | start_enabled | join_enabled | join_text | goto_enabled |
        | <game> | <creator> | <creator>,<newcomer> | CREATED | <se2>         | <je2>        | <jt2>     | <ge2>        |
      When <creator> starts "<game>" <sBeFe>
      Then the following games are displayed
        | name   | creator   | players              | state   | start_enabled | join_enabled | join_text | goto_enabled |
        | <game> | <creator> | <creator>,<newcomer> | STARTED | <se3>         | <je3>        | <jt3>     | <ge3>        |
      Examples: (I) join an (existing) game created by (other)
        | game        | creator     | sBeFe            | newcomer | jBeFe             | se1   | je1  | jt1  | ge1   | se2   | je2  | jt2            | ge2   | se3   | je3  | jt3          | ge3  |
        | test-game-1 | test-user-1 | from the backend | qa-user  | from the frontend | false | true | join | false | false | null | already joined | false | false | null | game started | true |
      Examples: (other) joins an (existing) game created by (me)
        | game        | creator     | sBeFe             | newcomer    | jBeFe            | se1   | je1  | jt1            | ge1   | se2   | je2  | jt2            | ge2   | se3   | je3  | jt3          | ge3   |
        | test-game-2 | qa-user     | from the frontend | test-user-1 | from the backend | false | null | already joined | false | true  | null | already joined | false | false | null | game started | true  |
      Examples: (other) joins an (existing) game created by (other)
        | game        | creator     | sBeFe             | newcomer    | jBeFe            | se1   | je1  | jt1            | ge1   | se2   | je2  | jt2            | ge2   | se3   | je3  | jt3          | ge3   |
        | test-game-1 | test-user-1 | from the backend  | test-user-2 | from the backend | false | true | join           | false | false | true | join           | false | false | null | game started | false |

    Scenario Outline: Verify Game Details Displayed - On a newly created game
      When <creator> creates a game named "<game>" <cBeFe>
      When qa-user clicks on game details link for "<game>"
      Then i am on the on game details page for "<game>"
      And the following games are displayed
        | name   | creator   | players   | state   | start_enabled | join_enabled | join_text | goto_enabled |
        | <game> | <creator> | <creator> | CREATED | <se1>         | <je1>        | <jt1>     | <ge1>        |
      When <newcomer> joins "<game>" <jBeFe>
      Then the following games are displayed
        | name   | creator   | players              | state   | start_enabled | join_enabled | join_text | goto_enabled |
        | <game> | <creator> | <creator>,<newcomer> | CREATED | <se2>         | <je2>        | <jt2>     | <ge2>        |
      When <creator> starts "<game>" <sBeFe>
      Then the following games are displayed
        | name   | creator   | players              | state   | start_enabled | join_enabled | join_text | goto_enabled |
        | <game> | <creator> | <creator>,<newcomer> | STARTED | <se3>         | <je3>        | <jt3>     | <ge3>        |
      Examples: (I) joins an (new) game created by (other)
        | game    | creator     | cBeFe             | newcomer    | jBeFe             | sBeFe             | se1   | je1  | jt1            | ge1   | se2   | je2  | jt2            | ge2   | se3   | je3  | jt3          | ge3   |
        | newGame | test-user-1 | from the backend  | qa-user     | from the frontend | from the backend  | false | true | join           | false | false | null | already joined | false | false | null | game started | true  |
      Examples: (other) joins an (new) game created by (me)
        | game    | creator     | cBeFe             | newcomer    | jBeFe             | sBeFe             | se1   | je1  | jt1            | ge1   | se2   | je2  | jt2            | ge2   | se3   | je3  | jt3          | ge3   |
        | newGame | qa-user     | from the frontend | test-user-1 | from the backend  | from the frontend | false | null | already joined | false | true  | null | already joined | false | false | null | game started | true  |
      Examples: (other) joins an (new) game created by (other)
        | game    | creator     | cBeFe             | newcomer    | jBeFe             | sBeFe             | se1   | je1  | jt1            | ge1   | se2   | je2  | jt2            | ge2   | se3   | je3  | jt3          | ge3   |
        | newGame | test-user-1 | from the backend  | test-user-2 | from the backend  | from the backend  | false | true | join           | false | false | true | join           | false | false | null | game started | false |


    Scenario Outline: Verify Game Details Displayed - Direct url access and refresh
      When qa-user directly access the game-details page for "<game>"
      And qa-user reloads the page
      Then i am on the on game details page for "<game>"
      And the following games are displayed
        | name   | creator   | players   | state   | start_enabled | join_enabled | join_text | goto_enabled |
        | <game> | <creator> | <creator> | CREATED | <se1>         | <je1>        | <jt1>     | <ge1>        |
      When <newcomer> joins "<game>" <jBeFe>
      Then the following games are displayed
        | name   | creator   | players              | state   | start_enabled | join_enabled | join_text | goto_enabled |
        | <game> | <creator> | <creator>,<newcomer> | CREATED | <se2>         | <je2>        | <jt2>     | <ge2>        |
      When <creator> starts "<game>" <sBeFe>
      Then the following games are displayed
        | name   | creator   | players              | state   | start_enabled | join_enabled | join_text | goto_enabled |
        | <game> | <creator> | <creator>,<newcomer> | STARTED | <se3>         | <je3>        | <jt3>     | <ge3>        |
      Examples: (I) joins an (existing) game created by (other)
        | game        | creator     | sBeFe             | newcomer    | jBeFe             | se1   | je1  | jt1            | ge1   | se2   | je2  | jt2            | ge2   | se3   | je3  | jt3          | ge3   |
        | test-game-1 | test-user-1 | from the backend  | qa-user     | from the frontend | false | true | join           | false | false | null | already joined | false | false | null | game started | true  |
      Examples: (other) joins an (existing) game created by (me)
        | game        | creator     | sBeFe             | newcomer    | jBeFe             | se1   | je1  | jt1            | ge1   | se2   | je2  | jt2            | ge2   | se3   | je3  | jt3          | ge3   |
        | test-game-2 | qa-user     | from the frontend | test-user-1 | from the backend  | false | null | already joined | false | true  | null | already joined | false | false | null | game started | true  |
      Examples: (other) joins an (existing) game created by (other)
        | game        | creator     | sBeFe             | newcomer    | jBeFe             | se1   | je1  | jt1            | ge1   | se2   | je2  | jt2            | ge2   | se3   | je3  | jt3          | ge3   |
        | test-game-1 | test-user-1 | from the backend  | test-user-2 | from the backend  | false | true | join           | false | false | true | join           | false | false | null | game started | false |
