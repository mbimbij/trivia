Feature: List games

  Scenario: display games list
    Given 2 existing games
    And a logged-in test user
    When test user accesses games list page
    Then the games are displayed