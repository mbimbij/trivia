Feature: On-Going Game Page

  Background:
    Given previous test data cleared
    And 2 existing games
    And "test-game-2" started
    And a logged-in test user on the game-list page
    And qa-user clicks on "goto" button for "test-game-2"
    And i am on the on game page for "test-game-2"
    And current game is "test-game-2"
    And current player is "qa-user"

  Scenario: Go to game from game-list page
    Then the element with testid "game-header-section" is visible
    And the element with testid "game-logs-section" is visible

  Scenario: Play a turn outside penalty box
    Given current player is not in the penalty box
    Then the element with testid "roll-dice" is visible
    And the element with testid "answer-question" is not visible
    And displayed game logs ends with logs matching
      | Game Id\\(value=[0-9]*\\) started |
      | qa-user is the current player     |
    When qa-user clicks on the element with testid "roll-dice"
    Then the element with testid "roll-dice" is not visible
    And the element with testid "answer-question" is visible
    And displayed game logs ends with logs matching
      | qa-user is the current player  |
      | They have rolled a \\d         |
      | qa-user's new location is \\d+ |
      | The category is .*             |
      | question .*                    |
    When qa-user clicks on answer A
    Then the element with testid "roll-dice" is not visible
    And the element with testid "answer-question" is not visible
    And displayed game logs ends with logs matching
      | question .*                       |
      | Answer was correct!!!!            |
      | qa-user now has 1 Gold Coins.     |
      | test-user-1 is the current player |
