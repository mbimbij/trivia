package com.adaptionsoft.games.domain.pageObjects;

import com.adaptionsoft.games.trivia.game.domain.AnswerCode;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;

import java.util.List;

public class OngoingGamePage extends PageWithDynamicUrl {
    public static final String ROLL_DICE_BUTTON_TESTID = "roll-dice";
    public static final String ROLL_DICE_RESULTS_SECTION_TESTID = "roll-dice-results-section";
    public static final String ROLL_DICE_RESULTS_MESSAGE_TESTID = "roll-dice-results-message";
    public static final String DRAW_QUESTION_BUTTON_TESTID = "draw-question";
    public static final String ANSWER_QUESTION_SECTION_TESTID = "answer-question";
    public static final String ANSWER_QUESTION_RESULTS_SECTION_TESTID = "answer-question-results";
    public static final String ANSWER_QUESTION_RESULTS_IS_CORRECT_PROMPT_TESTID = "is-answer-correct-prompt";
    public static final String VALIDATION_BUTTON_TESTID = "validate";
    public static final String OK_SECTION_TESTID = "ok-section";
    public static final String ERROR_SECTION_TESTID = "error-section";
    public static final String LOADING_SECTION_TESTID = "loading-section";
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
