Feature: Game Creation

  Background:
    And previous test data cleared
    And qa-user on the game-list page

  Rule: Game created by qa-user
    Scenario: actions on a game created without changing player's name
      When qa-user creates a game named "newGame" from the frontend
      Then qa-user sees the following games, filtered for creators "qa-user"
        | name    | creator | players | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | newGame | qa-user | qa-user | CREATED | false         | null         | already joined | false        | true           |
    Scenario: actions on a game created using a different name
      When qa-user creates a game named "newGame", with username "other-name", from the frontend
      Then qa-user sees the following games, filtered for creators "other-name"
        | name    | creator    | players    | state   | start_enabled | join_enabled | join_text      | goto_enabled | delete_enabled |
        | newGame | other-name | other-name | CREATED | false         | null         | already joined | false        | true           |
