package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.domain.views.DisplayedGame;
import com.adaptionsoft.games.trivia.domain.AnswerCode;
import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.microsoft.playwright.ConsoleMessage;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Predicate;

@Slf4j
public class FrontendActor extends TestActor {
    private final Page page;
    private final List<ConsoleMessage> currentScenarioConsoleMessages = new ArrayList<>();
    private final String ROLL_DICE_BUTTON_TESTID = "roll-dice";
    private final String ANSWER_QUESTION_SECTION_TESTID = "answer-question";
    @Getter
    private boolean isLoggedIn = false;
    private final String frontendUrlBase;
    private final String email;
    private final String password;

    public FrontendActor(@NotBlank String id,
                         @NotBlank String name,
                         Page page,
                         String frontendUrlBase,
                         String email,
                         String password) {
        super(id, name);
        this.page = page;
        this.frontendUrlBase = frontendUrlBase;
        this.email = email;
        this.password = password;
    }

    public List<String> getErrorLogs() {
        return currentScenarioConsoleMessages.stream()
                .filter(consoleMessage -> Objects.equals("error", consoleMessage.type()))
                .map(ConsoleMessage::text)
                .toList();
    }

    public void registerBrowserLogs() {
        this.page.onConsoleMessage(e -> isAdd(e));
    }

    private boolean isAdd(ConsoleMessage e) {
        return currentScenarioConsoleMessages.add(e);
    }

    @Override
    public void createGame(String gameName) {
        page.getByTestId("create-game-name").fill(gameName);
        Locator button = page.getByTestId("create-game-validate");
        PlaywrightAssertions.assertThat(button).isVisible();
        PlaywrightAssertions.assertThat(button).isEnabled();
        button.click();
    }

    @Override
    public void join(int gameId) {
        String joinButtonTestid = "%s-button-%d".formatted("join", gameId);
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
    public List<DisplayedGame> getDisplayedGamesForUsers(Collection<String> userNames) {
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

    public void login() {
        if (!isLoggedIn()) {
            log.info("%s is logging in".formatted(name));
            page.navigate(frontendUrlBase + "/authentication");
            page.locator("css=.firebaseui-idp-password").click();
            page.locator("css=#ui-sign-in-email-input").fill(email);
            page.locator("css=.firebaseui-id-submit").click();
            page.locator("css=#ui-sign-in-password-input").fill(password);
            page.locator("css=.firebaseui-id-submit").click();
            page.waitForURL(frontendUrlBase + "/games");
            setLoggedIn(true);
        }
        log.info("%s is logged in".formatted(name));
    }

    private void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public void clearConsoleLogs() {
        currentScenarioConsoleMessages.clear();
    }
}
