package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.*;
import com.adaptionsoft.games.trivia.domain.AnswerCode;
import com.adaptionsoft.games.utils.TestUtils;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.awaitility.Awaitility.await;

public class OngoingGameStepDefs {

    private final TestContext testContext;
    private final TestProperties testProperties;
    private final Page page;
    private final FrontendActor qaActor;
    private final TestRunnerActor testRunnerActor;
    private final BackendActor backendActor1;
    private final BackendActor qaBackendActor;
    private Integer gameId;

    public OngoingGameStepDefs(TestContext testContext,
                               TestProperties testProperties,
                               Page page,
                               FrontendActor qaActor,
                               TestRunnerActor testRunnerActor,
                               @Qualifier("backendActor1") BackendActor backendActor1,
                               @Qualifier("qaBackendActor") BackendActor qaBackendActor) {
        this.testContext = testContext;
        this.testProperties = testProperties;
        this.page = page;
        this.qaActor = qaActor;
        this.testRunnerActor = testRunnerActor;
        this.backendActor1 = backendActor1;
        this.qaBackendActor = qaBackendActor;
    }

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

    @Given("game started")
    public void started() {
        // TODO insert a started game in database directly
        backendActor1.join(gameId);
        qaBackendActor.start(gameId);
    }

    @When("i am on the on game page for {string}")
    public void iAmOnTheOnGamePageFor(String gameName) {
        Integer gameId = testContext.getGameIdForName(gameName);
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
        testRunnerActor.setLoadedDiceForGame(gameId, number);
    }

    @And("current game is {string}")
    public void currentGameIs(String gameName) {
        this.gameId = testContext.getGameIdForName(gameName);
    }

    @And("qa-user goes to the game")
    public void qaUserGoesToTheGame() {
        qaActor.gotoGame(gameId);
    }

    @Given("qa-user is put in the penalty box")
    public void qaUserIsPutInThePenaltyBox() {
        testRunnerActor.putQaUserInPenaltyBox(gameId, testProperties.getQaUserId());
    }

}
