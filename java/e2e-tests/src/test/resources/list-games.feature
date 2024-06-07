Feature: List games

  Background:
    Given previous test data cleared
    And 2 existing games
    And a logged-in test user on the game-list page

  Scenario: display games list
    Then the following games are displayed for users "test-user-1, qa-user"
      | name        | creator     | players     | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
      | test-game-1 | test-user-1 | test-user-1 | created | null          | true         | join           | false        | false          |
      | test-game-2 | qa-user     | qa-user     | created | null          | null         | already joined | false        | true           |

  Rule: Changes on another user's game
    Scenario: another player creating a game updates the display
      When "test-user-1" creates a game named "newGame"
      Then the following games are displayed for users "test-user-1, qa-user"
        | name        | creator     | players     | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-1 | test-user-1 | test-user-1 | created | null          | true         | join           | false        | false          |
        | test-game-2 | qa-user     | qa-user     | created | null          | null         | already joined | false        | true           |
        | newGame     | test-user-1 | test-user-1 | created | null          | true         | join           | false        | false          |
      When "test-user-1" deletes the game named "test-game-1"
      Then the following games are displayed for users "test-user-1, qa-user"
        | name        | creator     | players     | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user     | qa-user     | created | null          | null         | already joined | false        | true           |
        | newGame     | test-user-1 | test-user-1 | created | null          | true         | join           | false        | false          |
      When "test-user-1" deletes the game named "newGame"
      Then the following games are displayed for users "test-user-1, qa-user"
        | name        | creator     | players     | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user     | qa-user     | created | null          | null         | already joined | false        | true           |

    Scenario: join & start game of other players updates the display
      When "test-user-2" joins "test-game-1"
      Then the following games are displayed for users "test-user-1, qa-user"
        | name        | creator     | players                 | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-1 | test-user-1 | test-user-1,test-user-2 | created | null          | true         | join           | false        | false          |
        | test-game-2 | qa-user     | qa-user                 | created | null          | null         | already joined | false        | true           |
#      TODO create a seperate independent test for starting a game
      When test-user-1 starts test-game-1
      Then the following games are displayed for users "test-user-1, qa-user"
        | name        | creator     | players                 | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-1 | test-user-1 | test-user-1,test-user-2 | started | null          | null         | game started   | false        | false          |
        | test-game-2 | qa-user     | qa-user                 | created | null          | null         | already joined | false        | true           |

  Rule: Changes on one's own game
    Scenario: create, join & start game created by qa-user updates the UI
      When "qa-user" creates a game named "newGame"
      Then the following games are displayed for users "qa-user"
        | name        | creator | players | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user | qa-user | created | null          | null         | already joined | false        | true           |
        | newGame     | qa-user | qa-user | created | null          | null         | already joined | false        | true           |
      When "test-user-1" joins "test-game-2"
      Then the following games are displayed for users "qa-user"
        | name        | creator | players             | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user | qa-user,test-user-1 | created | true          | null         | already joined | false        | true           |
        | newGame     | qa-user | qa-user             | created | null          | null         | already joined | false        | true           |
#      TODO create a seperate independent test
      When "test-user-1" joins "newGame"
      Then the following games are displayed for users "qa-user"
        | name        | creator | players             | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user | qa-user,test-user-1 | created | true          | null         | already joined | false        | true           |
        | newGame     | qa-user | qa-user,test-user-1 | created | true          | null         | already joined | false        | true           |
#      TODO create a seperate independent test
      When qa-user clicks on "start" button for "test-game-2"
      Then the following games are displayed for users "qa-user"
        | name        | creator | players             | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | test-game-2 | qa-user | qa-user,test-user-1 | started | null          | null         | game started   | true         | true           |
        | newGame     | qa-user | qa-user,test-user-1 | created | true          | null         | already joined | false        | true           |
#      TODO create a seperate independent test
      When qa-user clicks on "start" button for "newGame"
      Then the following games are displayed for users "qa-user"
        | name        | creator | players             | state   | start_enabled | join_enabled | join_text    | goto_enabled | delete_enabled |
        | test-game-2 | qa-user | qa-user,test-user-1 | started | null          | null         | game started | true         | true           |
        | newGame     | qa-user | qa-user,test-user-1 | started | null          | null         | game started | true         | true           |
#      TODO create a seperate independent test
      When qa-user clicks on "delete" button for "test-game-2"
      Then the following games are displayed for users "qa-user"
        | name    | creator | players             | state   | start_enabled | join_enabled | join_text    | goto_enabled | delete_enabled |
        | newGame | qa-user | qa-user,test-user-1 | started | null          | null         | game started | true         | true           |
#      TODO create a seperate independent test
      When qa-user clicks on "delete" button for "newGame"
      Then the following games are displayed for users "qa-user"
        | name | creator | players | state | start_enabled | join_enabled | join_text | goto_enabled | delete_enabled |
