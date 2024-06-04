Feature: List games

  Scenario: display games list
    Given the following games
    When accessing games list
    Then the games are displayed