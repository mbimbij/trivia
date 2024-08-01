package com.adaptionsoft.games.domain.pageObjects;

import com.adaptionsoft.games.trivia.domain.AnswerCode;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;

import java.util.List;

public class OngoingGamePage extends PageWithDynamicUrl {
    public static final String ROLL_DICE_BUTTON_TESTID = "roll-dice";
    public static final String ROLL_DICE_BACKHAND_SECTION = "roll-dice-backhand-section";
    public static final String ROLL_DICE_BACKHAND_MESSAGE_TESTID = "roll-dice-backhand-message";
    public static final String DRAW_QUESTION_BUTTON_TESTID = "draw-question";
    public static final String ANSWER_QUESTION_SECTION_TESTID = "answer-question";
    public static final String ANSWER_BACKHAND_SECTION_TESTID = "backhand";
    public static final String ANSWER_BACKHAND_IS_CORRECT_PROMPT = "is-answer-correct";
    public static final String VALIDATION_BUTTON_TESTID = "validate";
    public static final String OK_SECTION = "ok-section";
    public static final String ERROR_SECTION = "error-section";
    public static final String LOADING_SECTION = "loading-section";
    public static final String WINNER_PROMPT_SECTION_TESTID = "winner-prompt-section";

    public OngoingGamePage(Page page, String urlTemplate) {
        super(urlTemplate,page);
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

    public void answerQuestionWith(AnswerCode answerCode) {
        clickButtonByTestid("answer-%s".formatted(answerCode));
    }

}
