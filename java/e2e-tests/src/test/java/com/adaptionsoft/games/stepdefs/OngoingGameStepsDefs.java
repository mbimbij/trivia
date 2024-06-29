package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.TestContext;
import com.adaptionsoft.games.domain.TestProperties;
import com.adaptionsoft.games.trivia.domain.AnswerCode;
import com.adaptionsoft.games.utils.TestUtils;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
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
public class OngoingGameStepsDefs {

    private final TestContext testContext;

    private final TestProperties testProperties;

    private static List<String> getGameLogs() {
        List<String> actualLogs = StepsDefs.getPage().querySelectorAll(".log-line")
                .stream()
                .map(ElementHandle::textContent).toList();
        return actualLogs;
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

    @When("i am on the on game page for {string}")
    public void iAmOnTheOnGamePageFor(String gameName) {
        Integer gameId = testContext.getGameByName(gameName).id();
        String url = (testProperties.getFrontendUrlBase() + "/games/%d").formatted(gameId);
        PlaywrightAssertions.assertThat(StepsDefs.getPage()).hasURL(url);
    }


    // TODO refacto en "qa-user {isOrNot} in the penalty box
    @Given("current player {isOrNot} in the penalty box")
    public void currentPlayerIsInThePenaltyBox(IS_OR_NOT isOrNotInThePenaltyBox) {
        // TODO Récupérer les données utilisateur depuis la golden source (le backend) via un service ou repo
        final boolean inPenaltyBox = testContext.getGameByName("test-game-2").currentPlayer().isInPenaltyBox();
        switch (isOrNotInThePenaltyBox) {
            case IS -> assertThat(inPenaltyBox).isTrue();
            case IS_NOT -> assertThat(inPenaltyBox).isFalse();
        }
    }

    @And("the element with testid {string} {isOrNot} visible")
    public void theFollowingElementsAreVisible(String testId, IS_OR_NOT isOrNotVisible) {
        switch (isOrNotVisible) {
            case IS -> PlaywrightAssertions.assertThat(StepsDefs.getPage().getByTestId(testId)).isVisible();
            case IS_NOT -> PlaywrightAssertions.assertThat(StepsDefs.getPage().getByTestId(testId)).not().isVisible();
        }
    }

    @Then("qa-user clicks on answer {answerCode}")
    public void qaUserClicksOnTheAnswer(AnswerCode answerCode) {
        Locator locator = StepsDefs.getPage().getByTestId("answer-%s".formatted(answerCode));
        PlaywrightAssertions.assertThat(locator).isVisible();
        PlaywrightAssertions.assertThat(locator).isEnabled();
        locator.click();
    }

    @And("displayed game logs end with logs matching")
    public void displayedGameLogsEndsWithLogsMatching(List<String> expectedLogs) {
        await().atMost(Duration.ofSeconds(5))
                .pollInterval(TestUtils.pollInterval)
                .untilAsserted(
                        () -> verifyGameLogsMatch(getGameLogs(), expectedLogs)
                );
    }

}
