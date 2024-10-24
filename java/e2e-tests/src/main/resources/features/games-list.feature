Feature: List games

  Background:
    And previous test data cleared
    And 2 existing games
    And qa-user on the game-list page
    And qa-user name was not changed

  Scenario: display games list
    Then qa-user sees the following games, filtered for creators "test-user-1, qa-user"
      | name        | creator     | players     | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
      | test-game-1 | test-user-1 | test-user-1 | CREATED | false          | true         | join           | false        | false          |
      | test-game-2 | qa-user     | qa-user     | CREATED | false          | null         | already joined | false        | true           |

  Scenario: start button still displayed after reloading page (a bug present at some point)
    When test-user-1 joins "test-game-2" from the backend
    Then qa-user sees the following games, filtered for creators "qa-user"
      | name        | creator | players             | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
      | test-game-2 | qa-user | qa-user,test-user-1 | CREATED | true          | null         | already joined | false        | true           |
    When qa-user reloads the page
    Then qa-user sees the following games, filtered for creators "qa-user"
      | name        | creator | players             | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
      | test-game-2 | qa-user | qa-user,test-user-1 | CREATED | true          | null         | already joined | false        | true           |

  Rule: Changes on another user's game
    Scenario: another player creating a game updates the display
      When test-user-1 creates a game named "newGame" from the backend
      Then qa-user sees the following games, filtered for creators "test-user-1, qa-user"
        | name        | creator     | players     | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-1 | test-user-1 | test-user-1 | CREATED | false          | true         | join           | false        | false          |
        | test-game-2 | qa-user     | qa-user     | CREATED | false          | null         | already joined | false        | true           |
        | newGame     | test-user-1 | test-user-1 | CREATED | false          | true         | join           | false        | false          |
      When "test-user-1" deletes "test-game-1" from the backend
      Then qa-user sees the following games, filtered for creators "test-user-1, qa-user"
        | name        | creator     | players     | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user     | qa-user     | CREATED | false          | null         | already joined | false        | true           |
        | newGame     | test-user-1 | test-user-1 | CREATED | false          | true         | join           | false        | false          |
      When "test-user-1" deletes "newGame" from the backend
      Then qa-user sees the following games, filtered for creators "test-user-1, qa-user"
        | name        | creator | players | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user | qa-user | CREATED | false          | null         | already joined | false        | true           |

    Scenario: join & start game of other players updates the display
      When test-user-2 joins "test-game-1" from the backend
      Then qa-user sees the following games, filtered for creators "test-user-1, qa-user"
        | name        | creator     | players                 | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-1 | test-user-1 | test-user-1,test-user-2 | CREATED | false          | true         | join           | false        | false          |
        | test-game-2 | qa-user     | qa-user                 | CREATED | false          | null         | already joined | false        | true           |
#      TODO create a seperate independent test for starting a game
      When test-user-1 starts "test-game-1" from the backend
      Then qa-user sees the following games, filtered for creators "test-user-1, qa-user"
        | name        | creator     | players                 | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-1 | test-user-1 | test-user-1,test-user-2 | STARTED | false          | null         | game started   | false        | false          |
        | test-game-2 | qa-user     | qa-user                 | CREATED | false          | null         | already joined | false        | true           |

  Rule: Game created by qa-user
    Scenario: actions on a game created without changing player's name
      When qa-user creates a game named "newGame" from the backend
      Then qa-user sees the following games, filtered for creators "qa-user"
        | name        | creator | players | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user | qa-user | CREATED | false         | null         | already joined | false        | true           |
        | newGame     | qa-user | qa-user | CREATED | false         | null         | already joined | false        | true           |
      When test-user-1 joins "test-game-2" from the backend
      Then qa-user sees the following games, filtered for creators "qa-user"
        | name        | creator | players             | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user | qa-user,test-user-1 | CREATED | true          | null         | already joined | false        | true           |
        | newGame     | qa-user | qa-user             | CREATED | false         | null         | already joined | false        | true           |
#      TODO create a seperate independent test
      When test-user-1 joins "newGame" from the backend
      Then qa-user sees the following games, filtered for creators "qa-user"
        | name        | creator | players             | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user | qa-user,test-user-1 | CREATED | true          | null         | already joined | false        | true           |
        | newGame     | qa-user | qa-user,test-user-1 | CREATED | true          | null         | already joined | false        | true           |
#      TODO create a seperate independent test
      When qa-user starts "test-game-2" from the frontend
      Then qa-user sees the following games, filtered for creators "qa-user"
        | name        | creator | players             | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user | qa-user,test-user-1 | STARTED | false         | null         | game started   | true         | true           |
        | newGame     | qa-user | qa-user,test-user-1 | CREATED | true          | null         | already joined | false        | true           |
#      TODO create a seperate independent test
      When qa-user starts "newGame" from the frontend
      Then qa-user sees the following games, filtered for creators "qa-user"
        | name        | creator | players             | state   | start_enabled | join_enabled | join_text    | goto_enabled | delete_enabled |
        | test-game-2 | qa-user | qa-user,test-user-1 | STARTED | false         | null         | game started | true         | true           |
        | newGame     | qa-user | qa-user,test-user-1 | STARTED | false         | null         | game started | true         | true           |
#      TODO create a seperate independent test
      When qa-user deletes "test-game-2" from the frontend
      Then qa-user sees the following games, filtered for creators "qa-user"
        | name    | creator | players             | state   | start_enabled | join_enabled | join_text    | goto_enabled | delete_enabled |
        | newGame | qa-user | qa-user,test-user-1 | STARTED | false         | null         | game started | true         | true           |
#      TODO create a seperate independent test
      When qa-user deletes "newGame" from the frontend
      Then qa-user sees the following games, filtered for creators "qa-user"
        | name | creator | players | state | start_enabled | join_enabled | join_text | goto_enabled | delete_enabled |
