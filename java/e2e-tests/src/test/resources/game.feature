Feature: On-Going Game Page

  Background:
    Given previous test data cleared
    And 2 existing games
    And "test-game-2" started
    And a logged-in test user on the game-list page
    And qa-user clicks on "goto" button for "test-game-2"
    And i am on the on game page for "test-game-2"

  Scenario: Go to game from game-list page
    Then the element with testid "game-header-section" is visible
    And the element with testid "game-logs-section" is visible

  Rule: Play a turn outside penalty box
    Scenario: Play a turn outside penalty box - Correct answer
      Given current player is not in the penalty box
      Then the element with testid "roll-dice" is visible
      And the element with testid "answer-question" is not visible
      And displayed game logs end with logs matching
        | Game Id\\(value=[0-9]*\\) started |
        | qa-user is the current player     |
      When qa-user clicks on the element with testid "roll-dice"
      Then the element with testid "roll-dice" is not visible
      And the element with testid "answer-question" is visible
      And displayed game logs end with logs matching
        | qa-user is the current player  |
        | They have rolled a \\d         |
        | qa-user's new location is \\d+ |
        | The category is .*             |
        | question .*                    |
      When qa-user clicks on answer A
      Then the element with testid "roll-dice" is not visible
      And the element with testid "answer-question" is not visible
      And displayed game logs end with logs matching
        | question .*                       |
        | Answer was correct!!!!            |
        | qa-user now has 1 Gold Coins.     |
        | test-user-1 is the current player |

    Scenario: Play a turn outside penalty box - First incorrect answer, Second correct answer
      When qa-user clicks on the element with testid "roll-dice"
      When qa-user clicks on answer B
      Then the element with testid "roll-dice" is not visible
      And the element with testid "answer-question" is visible
      And displayed game logs end with logs matching
        | question .*                       |
        | Question was incorrectly answered |
        | question .*                       |
      When qa-user clicks on answer B
      Then the element with testid "roll-dice" is not visible
      And the element with testid "answer-question" is not visible
      And displayed game logs end with logs matching
        | question .*                       |
        | Answer was correct!!!!            |
        | qa-user now has 1 Gold Coins.     |
        | test-user-1 is the current player |

    Scenario: Play a turn outside penalty box - First incorrect answer, Second incorrect answer
      When qa-user clicks on the element with testid "roll-dice"
      And qa-user clicks on answer B
      And qa-user clicks on answer C
      Then the element with testid "roll-dice" is not visible
      And the element with testid "answer-question" is not visible
      And displayed game logs end with logs matching
        | question .*                         |
        | Question was incorrectly answered   |
        | qa-user was sent to the penalty box |
        | test-user-1 is the current player   |

  Rule: Play a turn inside penalty box
    Scenario: Can roll dice from penalty box
      Given qa-user is put in the penalty box
      Then the element with testid "roll-dice" is visible
      And the element with testid "answer-question" is not visible

    Scenario: Play a turn inside penalty box - odd roll dice - stay in penalty box
      Given qa-user is put in the penalty box
      And a loaded dice returning a 3
      When qa-user clicks on the element with testid "roll-dice"
      Then the element with testid "roll-dice" is not visible
      And the element with testid "answer-question" is not visible
      And displayed game logs end with logs matching
        | qa-user is the current player                 |
        | qa-user was sent to the penalty box           |
        | They have rolled a 3                          |
        | qa-user is not getting out of the penalty box |
        | test-user-1 is the current player             |

    Scenario: An even roll dice gets you out of in penalty box
      Given qa-user is put in the penalty box
      And a loaded dice returning a 4
      When qa-user clicks on the element with testid "roll-dice"
      Then the element with testid "roll-dice" is not visible
      And the element with testid "answer-question" is visible
      And displayed game logs end with logs matching
        | qa-user is getting out of the penalty box |
        | qa-user's new location is 4               |
        | The category is .*                        |
        | question .* 0                             |

    Scenario: Answer correctly after getting out of the penalty box
      Given qa-user is put in the penalty box
      And a loaded dice returning a 4
      When qa-user clicks on the element with testid "roll-dice"
      And qa-user clicks on answer A
      And displayed game logs end with logs matching
        | qa-user is getting out of the penalty box |
        | qa-user's new location is 4               |
        | The category is .*                        |
        | question .* 0                             |
        | Answer was correct!!!!                    |
        | qa-user now has 1 Gold Coins.             |
        | test-user-1 is the current player         |

    Scenario: Answer incorrectly then correctly after getting out of the penalty box
      Given qa-user is put in the penalty box
      And a loaded dice returning a 4
      When qa-user clicks on the element with testid "roll-dice"
      And qa-user clicks on answer B
      And qa-user clicks on answer B
      And displayed game logs end with logs matching
        | qa-user is getting out of the penalty box |
        | qa-user's new location is 4               |
        | The category is .*                        |
        | question .* 0                             |
        | Question was incorrectly answered         |
        | question .* 1                             |
        | Answer was correct!!!!                    |
        | qa-user now has 1 Gold Coins.             |
        | test-user-1 is the current player         |

    Scenario: Answer incorrectly then incorrect again after getting out of the penalty box
      Given qa-user is put in the penalty box
      And a loaded dice returning a 4
      When qa-user clicks on the element with testid "roll-dice"
      And qa-user clicks on answer B
      And qa-user clicks on answer C
      And displayed game logs end with logs matching
        | qa-user is getting out of the penalty box |
        | qa-user's new location is 4               |
        | The category is .*                        |
        | question .* 0                             |
        | Question was incorrectly answered         |
        | question .* 1                             |
        | Question was incorrectly answered         |
        | qa-user was sent to the penalty box       |
        | test-user-1 is the current player         |