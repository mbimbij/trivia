package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.domain.views.DisplayedGame;
import com.adaptionsoft.games.stepdefs.StepDefs;
import com.adaptionsoft.games.trivia.domain.AnswerCode;
import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.microsoft.playwright.ConsoleMessage;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class FrontendActor extends TestActor {
    private final Page page;
    private final TestContext testContext;
    private final List<ConsoleMessage> currentScenarioConsoleMessages = new ArrayList<>();
    private final String ROLL_DICE_BUTTON_TESTID = "roll-dice";
    private final String ANSWER_QUESTION_SECTION_TESTID = "answer-question";

    public FrontendActor(@NotBlank String id, @NotBlank String name, Page page, TestContext testContext) {
        super(id, name);
        this.page = page;
        this.testContext = testContext;
    }

    public void registerBrowserLogs() {
        this.page.onConsoleMessage(currentScenarioConsoleMessages::add);
    }

    @Override
    public void join(GameResponseDto game) {
        String joinButtonTestid = "%s-button-%d".formatted("join", game.id());
        clickByTestid(joinButtonTestid);
    }

    @Override
    public void start(GameResponseDto game) {
        String startButtonTestid = "%s-button-%d".formatted("start", game.id());
        clickByTestid(startButtonTestid);
    }

    @Override
    public void rollDice() {
        Locator locator = page.getByTestId(ROLL_DICE_BUTTON_TESTID);
        PlaywrightAssertions.assertThat(locator).isVisible();
        PlaywrightAssertions.assertThat(locator).isEnabled();
        locator.click();
    }

    private void clickByTestid(String buttonTestId) {
        Locator button = page.getByTestId(buttonTestId);
        PlaywrightAssertions.assertThat(button).isVisible();
        button.click();
    }

    public void verifyCanSeeRollDiceButton() {
        verifyCanSeeElementWithTestid(ROLL_DICE_BUTTON_TESTID);
    }

    public void verifyCanSeeAnswerQuestionSection() {
        verifyCanSeeElementWithTestid(ANSWER_QUESTION_SECTION_TESTID);
    }

    public void verifyCanSeeElementWithTestid(String testId) {
        PlaywrightAssertions.assertThat(page.getByTestId(testId)).isVisible();
    }

    public void verifyCannotSeeElementWithTestid(String testId) {
        PlaywrightAssertions.assertThat(page.getByTestId(testId)).not().isVisible();
    }

    public List<String> getGameLogs() {
        return page.querySelectorAll(".log-line")
                .stream()
                .map(ElementHandle::textContent).toList();
    }

    public void verifyCannotSeeRollDiceButton() {
        verifyCannotSeeElementWithTestid(ROLL_DICE_BUTTON_TESTID);
    }

    public void verifyCannotSeeAnswerQuestionSection() {
        verifyCannotSeeElementWithTestid(ANSWER_QUESTION_SECTION_TESTID);
    }

    public void answerQuestionWith(AnswerCode answerCode) {
        Locator locator = page.getByTestId("answer-%s".formatted(answerCode));
        PlaywrightAssertions.assertThat(locator).isVisible();
        PlaywrightAssertions.assertThat(locator).isEnabled();
        locator.click();
    }

    public void gotoGame(Integer gameId) {
        String gotoButtonTestid = "%s-button-%d".formatted("goto", gameId);
        clickByTestid(gotoButtonTestid);
    }

    /**
     * @param userNames Pass null or an empty collection to get games for all users
     * @return
     */
    public List<DisplayedGame> getDisplayedGamesForUsers(Collection<String> userNames, StepDefs stepDefs) {
        Predicate<ElementHandle> predicate = (userNames == null || userNames.isEmpty())
                ? h -> true
                : h -> userNames.contains(h.querySelector(".creator-name").textContent().trim());
        return page.querySelectorAll(".game-row").stream()
                .filter(predicate)
                .map(this::convertToObject)
                .toList();
    }

    private DisplayedGame convertToObject(ElementHandle elementHandle) {
        return new DisplayedGame(
                elementHandle.querySelector(".name").textContent().trim(),
                elementHandle.querySelector(".creator-name").textContent().trim(),
                elementHandle.querySelector(".players-names").textContent().trim(),
                elementHandle.querySelector(".state").textContent().trim(),
                this.getButtonState(elementHandle, "start"),
                this.getButtonState(elementHandle, "join"),
                elementHandle.querySelector(".join").textContent().trim(),
                this.getButtonState(elementHandle, "goto"),
                this.getButtonState(elementHandle, "delete")
        );
    }

    private Boolean getButtonState(ElementHandle elementHandle, String buttonName) {
        return Optional.ofNullable(elementHandle.querySelector("." + buttonName + " button")).map(ElementHandle::isEnabled).orElse(null);
    }
}
