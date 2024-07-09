Feature: List games

  Background:
    Given previous test data cleared
    And 2 existing games
    And qa-user on the game-list page

  Scenario: display games list
    Then qa-user sees the following games, filtered for creators "test-user-1, qa-user"
      | name        | creator     | players     | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
      | test-game-1 | test-user-1 | test-user-1 | created | null          | true         | join           | false        | false          |
      | test-game-2 | qa-user     | qa-user     | created | null          | null         | already joined | false        | true           |
    And no error is displayed in the console

  Rule: Changes on another user's game
    Scenario: another player creating a game updates the display
      When test-user-1 creates a game named "newGame" from the backend
      Then qa-user sees the following games, filtered for creators "test-user-1, qa-user"
        | name        | creator     | players     | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-1 | test-user-1 | test-user-1 | created | null          | true         | join           | false        | false          |
        | test-game-2 | qa-user     | qa-user     | created | null          | null         | already joined | false        | true           |
        | newGame     | test-user-1 | test-user-1 | created | null          | true         | join           | false        | false          |
      And no error is displayed in the console
      When "test-user-1" deletes "test-game-1" from the backend
      Then qa-user sees the following games, filtered for creators "test-user-1, qa-user"
        | name        | creator     | players     | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user     | qa-user     | created | null          | null         | already joined | false        | true           |
        | newGame     | test-user-1 | test-user-1 | created | null          | true         | join           | false        | false          |
      And no error is displayed in the console
      When "test-user-1" deletes "newGame" from the backend
      Then qa-user sees the following games, filtered for creators "test-user-1, qa-user"
        | name        | creator     | players     | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user     | qa-user     | created | null          | null         | already joined | false        | true           |
      And no error is displayed in the console

    Scenario: join & start game of other players updates the display
      When test-user-2 joins "test-game-1" from the backend
      Then qa-user sees the following games, filtered for creators "test-user-1, qa-user"
        | name        | creator     | players                 | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-1 | test-user-1 | test-user-1,test-user-2 | created | null          | true         | join           | false        | false          |
        | test-game-2 | qa-user     | qa-user                 | created | null          | null         | already joined | false        | true           |
      And no error is displayed in the console
#      TODO create a seperate independent test for starting a game
      When test-user-1 starts "test-game-1" from the backend
      Then qa-user sees the following games, filtered for creators "test-user-1, qa-user"
        | name        | creator     | players                 | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-1 | test-user-1 | test-user-1,test-user-2 | started | null          | null         | game started   | false        | false          |
        | test-game-2 | qa-user     | qa-user                 | created | null          | null         | already joined | false        | true           |
      And no error is displayed in the console

  Rule: Changes on one's own game
    Scenario: create, join & start game created by qa-user updates the UI
#      TODO use the UI and not API when qa-user creates a game
      When qa-user creates a game named "newGame" from the frontend
      Then qa-user sees the following games, filtered for creators "qa-user"
        | name        | creator | players | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user | qa-user | created | null          | null         | already joined | false        | true           |
        | newGame     | qa-user | qa-user | created | null          | null         | already joined | false        | true           |
      And no error is displayed in the console
      When test-user-1 joins "test-game-2" from the backend
      Then qa-user sees the following games, filtered for creators "qa-user"
        | name        | creator | players             | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user | qa-user,test-user-1 | created | true          | null         | already joined | false        | true           |
        | newGame     | qa-user | qa-user             | created | null          | null         | already joined | false        | true           |
      And no error is displayed in the console
#      TODO create a seperate independent test
      When test-user-1 joins "newGame" from the backend
      Then qa-user sees the following games, filtered for creators "qa-user"
        | name        | creator | players             | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user | qa-user,test-user-1 | created | true          | null         | already joined | false        | true           |
        | newGame     | qa-user | qa-user,test-user-1 | created | true          | null         | already joined | false        | true           |
      And no error is displayed in the console
#      TODO create a seperate independent test
      When qa-user starts "test-game-2" from the frontend
      Then qa-user sees the following games, filtered for creators "qa-user"
        | name        | creator | players             | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user | qa-user,test-user-1 | started | null          | null         | game started   | true         | true           |
        | newGame     | qa-user | qa-user,test-user-1 | created | true          | null         | already joined | false        | true           |
      And no error is displayed in the console
#      TODO create a seperate independent test
      When qa-user starts "newGame" from the frontend
      Then qa-user sees the following games, filtered for creators "qa-user"
        | name        | creator | players             | state   | start_enabled | join_enabled | join_text    | goto_enabled | delete_enabled |
        | test-game-2 | qa-user | qa-user,test-user-1 | started | null          | null         | game started | true         | true           |
        | newGame     | qa-user | qa-user,test-user-1 | started | null          | null         | game started | true         | true           |
      And no error is displayed in the console
#      TODO create a seperate independent test
      When qa-user deletes "test-game-2" from the frontend
      Then qa-user sees the following games, filtered for creators "qa-user"
        | name    | creator | players             | state   | start_enabled | join_enabled | join_text    | goto_enabled | delete_enabled |
        | newGame | qa-user | qa-user,test-user-1 | started | null          | null         | game started | true         | true           |
      And no error is displayed in the console
#      TODO create a seperate independent test
      When qa-user deletes "newGame" from the frontend
      Then qa-user sees the following games, filtered for creators "qa-user"
        | name | creator | players | state | start_enabled | join_enabled | join_text | goto_enabled | delete_enabled |
      And no error is displayed in the console

# TODO test when qa-user joins a game: {own, others} x {newly-create, existing}