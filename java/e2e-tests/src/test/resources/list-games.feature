Feature: List games

  Background:
    Given a logged-in test user on the game-list page
    And a test user
    And 2 existing games

  Scenario: display games list
    Then the following games are displayed for users "test-user-1, qa-user"
      | name        | creator     | players     | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
      | test-game-1 | test-user-1 | test-user-1 | created | null          | true         | join           | false        | false          |
      | test-game-2 | qa-user     | qa-user     | created | null          | null         | already joined | false        | true           |

  Scenario: another player joining a game updates the display
    When test-user-2 joins test-game-1
    Then the following games are displayed for users "test-user-1, qa-user"
      | name        | creator     | players                 | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
      | test-game-1 | test-user-1 | test-user-1,test-user-2 | created | null          | true         | join           | false        | false          |
      | test-game-2 | qa-user     | qa-user                 | created | null          | null         | already joined | false        | true           |
    When test-user-1 starts test-game-1
    Then the following games are displayed for users "test-user-1, qa-user"
      | name        | creator     | players                 | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
      | test-game-1 | test-user-1 | test-user-1,test-user-2 | started | null          | null         | game started   | false        | false          |
      | test-game-2 | qa-user     | qa-user                 | created | null          | null         | already joined | false        | true           |

