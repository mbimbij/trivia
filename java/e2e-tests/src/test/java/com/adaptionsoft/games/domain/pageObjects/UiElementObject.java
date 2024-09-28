package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import lombok.Getter;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;

import java.util.Optional;

@Getter
public class UiElementObject {
    protected final Page page;

    public UiElementObject(Page page) {
        this.page = page;
    }

    public String getTextContentByTestid(String testid) {
        return page.getByTestId(testid).textContent().trim();
    }

    public void fillInputByTestId(String testid, String content) {
        page.getByTestId(testid).fill(content);
    }

    public void verifyInputContentByTestId(String testid, String expectedContent) {
        verifyPresenceByTestId(testid);
        String content = Optional.ofNullable(page.getByTestId(testid).getAttribute("ng-reflect-model"))
                .map(String::trim)
                .orElse("");
        Assertions.assertThat(content).isEqualTo(expectedContent);
    }

    @SneakyThrows
    public void verifyPresenceByTestId(String testid) {
        page.waitForSelector("[data-testid=%s]".formatted(testid));
    }

    public void verifyAbsenceByTestId(String testid) {
        PlaywrightAssertions.assertThat(page.getByTestId(testid)).not().isAttached();
    }

    public void clickButtonByTestid(String testid) {
        PlaywrightAssertions.assertThat(page.getByTestId(testid)).isVisible();
        PlaywrightAssertions.assertThat(page.getByTestId(testid)).isEnabled();
        clickElementByTestid(testid);
    }

    public void clickElementByTestid(String testId) {
        page.getByTestId(testId).click();
    }

    public void verifyButtonDisabledByTestid(String testId) {
        PlaywrightAssertions.assertThat(page.getByTestId(testId)).isDisabled();
    }

    public void verifyTextContent(String testId, String expectedContent) {
        PlaywrightAssertions.assertThat(page.getByTestId(testId)).hasText(expectedContent);
    }
}
