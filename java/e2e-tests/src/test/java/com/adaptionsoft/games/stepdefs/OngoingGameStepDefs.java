package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.FrontendActor;
import com.adaptionsoft.games.domain.TestContext;
import com.adaptionsoft.games.domain.TestProperties;
import com.adaptionsoft.games.domain.TestRunnerActor;
import com.adaptionsoft.games.trivia.domain.AnswerCode;
import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.adaptionsoft.games.utils.TestUtils;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.awaitility.Awaitility.await;

@RequiredArgsConstructor
public class OngoingGameStepDefs {

    private final TestContext testContext;
    private final TestProperties testProperties;
    private final Page page;
    private final FrontendActor qaActor;
    private final TestRunnerActor testRunnerActor;
    private String gameName;
    private GameResponseDto game;
    private Integer gameId;

    public static void verifyGameLogsMatch(List<String> actualLogs, List<String> expectedLogs) {
        assertSoftly(soft -> {
            soft.assertThat(expectedLogs).isNotEmpty();
            soft.assertThat(actualLogs).hasSizeGreaterThanOrEqualTo(expectedLogs.size());
        });

        List<String> logsToCompare = actualLogs.reversed().subList(0, expectedLogs.size()).reversed();

        assertThat(logsToCompare).zipSatisfy(
                expectedLogs,
                (actualLog, expectedLog) -> {
                    assertThat(actualLog).matches(expectedLog);
                });
    }

    @When("i am on the on game page for {string}")
    public void iAmOnTheOnGamePageFor(String gameName) {
        Integer gameId = testContext.getGameByName(gameName).id();
        String url = (testProperties.getFrontendUrlBase() + "/games/%d").formatted(gameId);
        PlaywrightAssertions.assertThat(page).hasURL(url);
    }

    @And("qa-user can see the element with testid {string}")
    public void theFollowingElementsAreVisible(String testId) {
        qaActor.verifyCanSeeElementWithTestid(testId);
    }

    @And("qa-user can see the roll dice button")
    public void qaUserCanSeeTheRollDiceButton() {
        qaActor.verifyCanSeeRollDiceButton();
    }

    @And("qa-user can see the answer question section")
    public void qaUserCanSeeTheAnswerQuestionSection() {
        qaActor.verifyCanSeeAnswerQuestionSection();
    }

    @And("qa-user cannot see the roll dice button")
    public void qaUserCannotSeeTheRollDiceButton() {
        qaActor.verifyCannotSeeRollDiceButton();
    }

    @Then("qa-user answers {answerCode}")
    public void qaUserClicksOnTheAnswer(AnswerCode answerCode) {
        qaActor.answerQuestionWith(answerCode);
    }

    @And("qa-user sees game logs ending as following")
    public void displayedGameLogsEndsWithLogsMatching(List<String> expectedLogs) {
        await().atMost(Duration.ofSeconds(5))
                .pollInterval(TestUtils.pollInterval)
                .untilAsserted(
                        () -> verifyGameLogsMatch(qaActor.getGameLogs(), expectedLogs)
                );
    }

    @When("qa-user rolls the dice")
    public void qaUserRollsTheDice() {
        qaActor.rollDice();
    }

    @And("qa-user cannot see the answer question section")
    public void qaUserCannotSeeTheAnswerQuestionSection() {
        qaActor.verifyCannotSeeAnswerQuestionSection();
    }

    @And("a loaded dice returning a {int}")
    public void aLoadedDiceReturningA(int number) {
        testRunnerActor.setLoadedDiceForGame(game.id(), number);
    }

    @And("current game is {string}")
    public void currentGameIs(String gameName) {
        this.gameName = gameName;
        this.game = testContext.getGameByName(gameName);
        this.gameId = this.game.id();
    }

    @And("qa-user goes to the game")
    public void qaUserGoesToTheGame() {
        qaActor.gotoGame(gameId);
    }

    @Given("qa-user is put in the penalty box")
    public void qaUserIsPutInThePenaltyBox() {
        testRunnerActor.putQaUserInPenaltyBox(gameId, testContext.getQaUserId());
    }

}
