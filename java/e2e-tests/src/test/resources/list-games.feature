Feature: List games

  Background:
    Given a logged-in test user on the game-list page
    And a test user
    And 2 existing games

  Scenario: display games list
    Then the following games are displayed
      | name        | creator     | players     | state   | start_enabled | join_enabled | goto_enabled | delete_enabled |
      | test-game-1 | test-user-1 | test-user-1 | created | null          | true         | false        | false          |
      | test-game-2 | test-user-1 | test-user-1 | created | null          | true         | false        | false          |

  Scenario: joining game updates display
    When test-user-2 joins test-game-1
    Then the following games are displayed
      | name        | creator     | players                 | state   | start_enabled | join_enabled | goto_enabled | delete_enabled |
      | test-game-1 | test-user-1 | test-user-1,test-user-2 | created | null          | true         | false        | false          |
      | test-game-2 | test-user-1 | test-user-1             | created | null          | true         | false        | false          |
