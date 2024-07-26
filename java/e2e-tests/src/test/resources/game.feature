Feature: On-Going Game Page

  Background:
    Given previous test data cleared
    And 2 existing games
    And current game is "test-game-2"
    And current game question deck has been replaced with a test one
    And qa-user on the game-list page
    And game started
    And qa-user goes to the game
    And i am on the on game page for "test-game-2"

  Scenario: Go to game from game-list page
    Then qa-user can see the element with testid "game-header-section"
    And qa-user can see the element with testid "game-logs-section"

  Scenario: No game logs duplication (cf backlog item 71676380)
    When qa-user clicks on "go-back" button
    And qa-user goes to the game
    When qa-user clicks on "go-back" button
    And qa-user goes to the game
    And qa-user sees game logs ending as following
      | qa-user was added                 |
      | They are player number 1          |
      | Game created                      |
      | test-user-1 was added             |
      | They are player number 2          |
      | Game Id\\(value=[0-9]*\\) started |
      | qa-user is the current player     |

  Scenario: Display error message when game not found
    When qa-user clicks on "go-back" button
    And qa-user goes to the game with id = -1
    Then ok section is not visible
    And loading section is not visible
    And error section is visible
    And error section text is "Error loading game with id -1"
    And error logs are expected in the console

  Rule: End of game display
    Scenario: Verify display when qa-user is the winner
      Given qa-user has 5 coins
      When qa-user rolls the dice
      And qa-user answers A
      And qa-user clicks on validation button
      Then winner prompt is visible
      And winner prompt text is "You won, Congratulations !"

    Scenario: Verify display when qa-user is not the winner
      Given test-user-1 has 5 coins
      And test-user-1 is the current player
      When test-user-1 rolls the dice from the backend
      When test-user-1 draws a question from the backend
      And test-user-1 answers A from the backend
      And test-user-1 clicks on validation button from the backend
      Then winner prompt is visible
      And winner prompt text is "test-user-1 won. Maybe next time."

  Rule: Play a turn outside penalty box
    Scenario: Play a turn outside penalty box - Correct answer
      Then qa-user can see the roll dice button
      And qa-user cannot see the answer question section
      And qa-user sees game logs ending as following
        | Game Id\\(value=[0-9]*\\) started |
        | qa-user is the current player     |
      When qa-user rolls the dice
      Then qa-user cannot see the roll dice button
      And qa-user can see the answer question section
      And qa-user cannot see the backhand section
      And qa-user sees game logs ending as following
        | qa-user is the current player  |
        | They have rolled a \\d         |
        | qa-user's new location is \\d+ |
        | The category is .*             |
        | question .*                    |
      When qa-user answers A
      Then qa-user cannot see the roll dice button
      And qa-user can see the answer question section
      And qa-user can see the backhand section
      And qa-user sees game logs ending as following
        | question .*                   |
        | Answer was correct!!!!        |
        | qa-user now has 1 Gold Coins. |
      When qa-user clicks on validation button
      Then qa-user cannot see the roll dice button
      And qa-user cannot see the answer question section
      And qa-user cannot see the backhand section
      And qa-user sees game logs ending as following
        | Answer was correct!!!!            |
        | qa-user now has 1 Gold Coins.     |
        | test-user-1 is the current player |

    Scenario: Play a turn outside penalty box - First incorrect answer, Second correct answer
      When qa-user rolls the dice
      And qa-user answers B
      Then qa-user cannot see the roll dice button
      And qa-user can see the answer question section
      And qa-user can see the backhand section
      And qa-user sees game logs ending as following
        | question .*                       |
        | Question was incorrectly answered |
      When qa-user clicks on validation button
      And qa-user sees game logs ending as following
        | question .*                       |
        | Question was incorrectly answered |
        | question .*                       |
      When qa-user answers B
      When qa-user clicks on validation button
      Then qa-user cannot see the roll dice button
      And qa-user cannot see the answer question section
      And qa-user sees game logs ending as following
        | question .*                       |
        | Answer was correct!!!!            |
        | qa-user now has 1 Gold Coins.     |
        | test-user-1 is the current player |

    Scenario: Play a turn outside penalty box - First incorrect answer, Second incorrect answer
      When qa-user rolls the dice
      And qa-user answers B
      And qa-user clicks on validation button
      And qa-user answers C
      And qa-user clicks on validation button
      Then qa-user cannot see the roll dice button
      And qa-user cannot see the answer question section
      And qa-user sees game logs ending as following
        | question .*                         |
        | Question was incorrectly answered   |
        | qa-user was sent to the penalty box |
        | test-user-1 is the current player   |

  Rule: Play a turn inside penalty box
    Scenario: Can roll dice from penalty box
      Given qa-user is put in the penalty box
      Then qa-user can see the roll dice button
      And qa-user cannot see the answer question section

    Scenario: Play a turn inside penalty box - odd roll dice - stay in penalty box
      Given qa-user is put in the penalty box
      And a loaded dice returning a 3
      When qa-user rolls the dice
      Then qa-user cannot see the roll dice button
      And qa-user cannot see the answer question section
      And qa-user sees game logs ending as following
        | qa-user is the current player                 |
        | qa-user was sent to the penalty box           |
        | They have rolled a 3                          |
        | qa-user is not getting out of the penalty box |
        | test-user-1 is the current player             |

    Scenario: An even roll dice gets you out of in penalty box
      Given qa-user is put in the penalty box
      And a loaded dice returning a 4
      When qa-user rolls the dice
      Then qa-user cannot see the roll dice button
      And qa-user can see the answer question section
      And qa-user sees game logs ending as following
        | qa-user is getting out of the penalty box |
        | qa-user's new location is 4               |
        | The category is .*                        |
        | question .* 0                             |

    Scenario: Answer correctly after getting out of the penalty box
      Given qa-user is put in the penalty box
      And a loaded dice returning a 4
      When qa-user rolls the dice
      And qa-user answers A
      And qa-user clicks on validation button
      And qa-user sees game logs ending as following
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
      When qa-user rolls the dice
      And qa-user answers B
      And qa-user clicks on validation button
      And qa-user answers B
      And qa-user clicks on validation button
      And qa-user sees game logs ending as following
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
      When qa-user rolls the dice
      And qa-user answers B
      And qa-user clicks on validation button
      And qa-user answers C
      And qa-user clicks on validation button
      And qa-user sees game logs ending as following
        | qa-user is getting out of the penalty box |
        | qa-user's new location is 4               |
        | The category is .*                        |
        | question .* 0                             |
        | Question was incorrectly answered         |
        | question .* 1                             |
        | Question was incorrectly answered         |
        | qa-user was sent to the penalty box       |
        | test-user-1 is the current player         |