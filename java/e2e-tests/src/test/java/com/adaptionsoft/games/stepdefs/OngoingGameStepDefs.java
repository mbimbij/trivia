package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.*;
import com.adaptionsoft.games.domain.pageObjects.Backend;
import com.adaptionsoft.games.domain.pageObjects.GameRowActions;
import com.adaptionsoft.games.domain.pageObjects.OngoingGamePage;
import com.adaptionsoft.games.trivia.domain.AnswerCode;
import com.adaptionsoft.games.utils.TestUtils;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;

import static com.adaptionsoft.games.domain.pageObjects.OngoingGamePage.WINNER_PROMPT_SECTION_TESTID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.awaitility.Awaitility.await;

@Slf4j
@RequiredArgsConstructor
public class OngoingGameStepDefs {

    private final TestContext testContext;
    private final TestProperties testProperties;
    private final Page page;
    private final Janitor janitor;
    private final ActorService actorService;
    private final GameRowActions gameRowActions;
    private final OngoingGamePage ongoingGamePage;
    private final Backend backend;
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

    @Given("game started")
    public void started() {
        // TODO insert a started game in database directly
        assertThat(gameId).isNotNull();
        backend.joinGame(gameId, actorService.getBackendActor1().toUserDto());
        backend.startGame(gameId, actorService.getQaBackendActor().getId());
    }

    @When("i am on the on game page for {string}")
    public void iAmOnTheOnGamePageFor(String gameName) {
        Integer gameId = testContext.getGameIdForName(gameName);
        String url = (testProperties.getFrontendUrlBase() + "/games/%d").formatted(gameId);
        PlaywrightAssertions.assertThat(page).hasURL(url);
    }

    @And("qa-user can see the element with testid {string}")
    public void theFollowingElementsAreVisible(String testId) {
        ongoingGamePage.verifyCanSeeElementWithTestid(testId);
    }

    @And("qa-user can see the roll dice button")
    public void qaUserCanSeeTheRollDiceButton() {
        ongoingGamePage.verifyCanSeeElementWithTestid(OngoingGamePage.ROLL_DICE_BUTTON_TESTID);
    }

    @And("qa-user can see the answer question section")
    public void qaUserCanSeeTheAnswerQuestionSection() {
        ongoingGamePage.verifyCanSeeElementWithTestid(OngoingGamePage.ANSWER_QUESTION_SECTION_TESTID);
    }

    @And("qa-user cannot see the roll dice button")
    public void qaUserCannotSeeTheRollDiceButton() {
        ongoingGamePage.verifyCannotSeeElementWithTestid(OngoingGamePage.ROLL_DICE_BUTTON_TESTID);
    }

    @Then("qa-user answers {answerCode}")
    public void qaUserClicksOnTheAnswer(AnswerCode answerCode) {
        ongoingGamePage.answerQuestionWith(answerCode);
    }

    @And("qa-user sees game logs ending as following")
    public void displayedGameLogsEndsWithLogsMatching(List<String> expectedLogs) {
        await().atMost(Duration.ofSeconds(5))
                .pollInterval(TestUtils.pollInterval)
                .untilAsserted(
                        () -> verifyGameLogsMatch(ongoingGamePage.getGameLogs(), expectedLogs)
                );
    }

    @When("qa-user rolls the dice")
    public void qaUserRollsTheDice() {
        ongoingGamePage.clickButtonByTestid(OngoingGamePage.ROLL_DICE_BUTTON_TESTID);
    }

    @And("qa-user cannot see the answer question section")
    public void qaUserCannotSeeTheAnswerQuestionSection() {
        ongoingGamePage.verifyCannotSeeElementWithTestid(OngoingGamePage.ANSWER_QUESTION_SECTION_TESTID);
    }

    @And("a loaded dice returning a {int}")
    public void aLoadedDiceReturningA(int number) {
        janitor.setLoadedDiceForGame(gameId, number);
    }

    @And("current game is {string}")
    public void currentGameIs(String gameName) {
        this.gameId = testContext.getGameIdForName(gameName);
    }

    @And("qa-user goes to the game")
    public void qaUserGoesToTheGame() {
        gameRowActions.goTo(gameId);
    }

    @Given("qa-user is put in the penalty box")
    public void qaUserIsPutInThePenaltyBox() {
        janitor.putQaUserInPenaltyBox(gameId, testProperties.getQaUserId());
    }

    @And("current game question deck has been replaced with a test one")
    public void currentGameQuestionDeckHasBeenReplacedWithATestOne() {
        assertThat(gameId).isNotNull();
        janitor.setLoadedQuestionDeckForGame(gameId);
    }

    @And("qa-user can see the backhand section")
    public void qaUserCanSeeTheAnswerBackendSection() {
        ongoingGamePage.verifyCanSeeElementWithTestid(OngoingGamePage.BACKHAND_SECTION_TESTID);
    }

    @And("qa-user cannot see the backhand section")
    public void qaUserCannotSeeTheBackhandSection() {
        ongoingGamePage.verifyCannotSeeElementWithTestid(OngoingGamePage.BACKHAND_SECTION_TESTID);
    }

    @When("qa-user clicks on validation button")
    public void qaUserClicksOnValidationButton() {
        ongoingGamePage.clickButtonByTestid(OngoingGamePage.VALIDATION_BUTTON_TESTID);
    }

    @When("qa-user clicks on \"go-back\" button")
    public void qaUserClicksOnButton() {
        ongoingGamePage.clickButtonByTestid(OngoingGamePage.GO_BACK_BUTTON_TESTID);
    }

    @Given("{actor} has {int} coins")
    public void qaUserHasCoins(Actor qaUser, int coinCount) {
        janitor.setCoinCount(gameId, qaUser.getId(), coinCount);
    }

    @Then("winner prompt is visible")
    public void winnerPromptIsVisible() {
        ongoingGamePage.verifyCanSeeElementWithTestid(WINNER_PROMPT_SECTION_TESTID);
    }

    @And("winner prompt text is {string}")
    public void winnerPromptTextIs(String expectedTextContent) {
        String actualTextContent = ongoingGamePage.getTextContentByTestid(WINNER_PROMPT_SECTION_TESTID);
        assertThat(actualTextContent).isEqualTo(expectedTextContent);
    }

    @And("{actor} is the current player")
    public void testUserIsTheCurrentPlayer(Actor actor) {
        janitor.setCurrentPlayer(gameId, actor.getId());
    }

    @When("{actor} rolls the dice from the backend")
    public void testUserRollsTheDiceFromTheBackend(Actor actor) {
        backend.rollDice(gameId, actor.getId());
    }

    @And("{actor} answers {answerCode} from the backend")
    public void testUserAnswersAFromTheBackend(Actor actor, AnswerCode answerCode) {
        backend.answerQuestion(gameId, actor.getId(), answerCode);
    }

    @And("{actor} clicks on validation button from the backend")
    public void testUserClicksOnValidationButtonFromTheBackend(Actor actor) {
        backend.validate(gameId, actor.getId());
    }

    @When("{actor} draws a question from the backend")
    public void testUserDrawsAQuestionFromTheBackend(Actor  actor) {
        backend.drawQuestion(gameId, actor.getId());
    }
}
