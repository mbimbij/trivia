Feature: On-Going Game Page

  Background:
    Given previous test data cleared
    And 2 existing games
    And "test-game-2" started
    And a logged-in test user on the game-list page

  Scenario: Go to game from game-list page
    When qa-user clicks on "goto" button for "test-game-2"
    Then i am on the on game page for "test-game-2"
    And i am on the on game page for "test-game-2"
    And the element with testid "game-header-section" is visible
    And the element with testid "game-logs-section" is visible
