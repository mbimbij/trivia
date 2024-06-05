Feature: List games

  Scenario: display games list
    Given a test user
    Given 2 existing games
    And a logged-in test user
    When test user accesses games list page
    Then the following games are displayed
      | name        | creator     | players     | state   | start_enabled | join_enabled | goto_enabled | delete_enabled |
      | test-game-1 | test-user-1 | test-user-1 | created | null          | true         | false        | false          |
      | test-game-2 | test-user-1 | test-user-1 | created | null          | true         | false        | false          |
