package com.adaptionsoft.games.domain.pageObjects;

import com.adaptionsoft.games.utils.TestUtils;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.Getter;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;

@Getter
public abstract class UiElementObject {
    protected final Page page;

    public UiElementObject(Page page) {
        this.page = page;
    }

    public String getTextContentByTestid(String testId) {
        return page.getByTestId(testId).textContent().trim();
    }

    public void fillInputByTestId(String testId, String content) {
        page.getByTestId(testId).clear();
        page.getByTestId(testId).fill(content);
    }

    public void verifyInputContentByTestId(String testId, String expectedContent) {
        verifyPresenceByTestId(testId);
        String content = page.getByTestId(testId).inputValue().trim();
        Assertions.assertThat(content).isEqualTo(expectedContent);
    }

    @SneakyThrows
    public void verifyPresenceByTestId(String testId) {
        page.waitForSelector("[data-testid=%s]".formatted(testId));
    }

    public void verifyAbsenceByTestId(String testId) {
        page.getByTestId(testId).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED));
        PlaywrightAssertions.assertThat(page.getByTestId(testId)).not().isAttached();
    }

    public void clickButtonByTestId(String testId) {
        PlaywrightAssertions.assertThat(page.getByTestId(testId)).isVisible();
        PlaywrightAssertions.assertThat(page.getByTestId(testId)).isEnabled();
        clickElementByTestId(testId);
    }

    public void clickElementByTestId(String testId) {
        page.getByTestId(testId).click();
    }

    public void verifyButtonDisabledByTestId(String testId) {
        PlaywrightAssertions.assertThat(page.getByTestId(testId)).isDisabled();
    }

    public void verifyTextContentByTestId(String testId, String expectedContent) {
        Awaitility.await().atMost(TestUtils.maxWaitDuration)
                .untilAsserted(() -> PlaywrightAssertions.assertThat(page.getByTestId(testId)).hasText(expectedContent));
    }
}
