package com.adaptionsoft.games.domain.pageObjects;

import com.adaptionsoft.games.domain.FrontendActor;
import com.adaptionsoft.games.trivia.domain.AnswerCode;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;

import java.util.List;

public class OngoingGamePage extends UiElementObject {
    public static final String ROLL_DICE_BUTTON_TESTID = "roll-dice";
    public static final String ANSWER_QUESTION_SECTION_TESTID = "answer-question";

    public OngoingGamePage(Page page) {
        super(page);
    }

    public void rollDice() {
        Locator locator = page.getByTestId(ROLL_DICE_BUTTON_TESTID);
        PlaywrightAssertions.assertThat(locator).isVisible();
        PlaywrightAssertions.assertThat(locator).isEnabled();
        locator.click();
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

    public void verifyCannotSeeRollDiceButton() {
        verifyCannotSeeElementWithTestid(ROLL_DICE_BUTTON_TESTID);
    }

    public void verifyCannotSeeAnswerQuestionSection() {
        verifyCannotSeeElementWithTestid(ANSWER_QUESTION_SECTION_TESTID);
    }

    public List<String> getGameLogs() {
        return page.querySelectorAll(".log-line")
                .stream()
                .map(ElementHandle::textContent).toList();
    }

    public void answerQuestionWith(AnswerCode answerCode, FrontendActor frontendActor) {
        Locator locator = page.getByTestId("answer-%s".formatted(answerCode));
        PlaywrightAssertions.assertThat(locator).isVisible();
        PlaywrightAssertions.assertThat(locator).isEnabled();
        locator.click();
    }
}
