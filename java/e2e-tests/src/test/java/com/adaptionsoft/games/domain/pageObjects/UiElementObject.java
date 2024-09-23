package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import lombok.Getter;

@Getter
public class UiElementObject {
    protected final Page page;

    public UiElementObject(Page page) {
        this.page = page;
    }

    public String getTextContentByTestid(String testid) {
        return page.getByTestId(testid).textContent().trim();
    }

    public void clickButtonByTestid(String testid) {
        PlaywrightAssertions.assertThat(page.getByTestId(testid)).isVisible();
        PlaywrightAssertions.assertThat(page.getByTestId(testid)).isEnabled();
        page.getByTestId(testid).click();
    }

    public void clickElementByTestid(int gameId, Page page) {
        page.getByTestId("game-details-%d".formatted(gameId)).click();
    }
}
