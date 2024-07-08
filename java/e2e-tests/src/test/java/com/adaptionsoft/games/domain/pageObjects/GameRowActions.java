package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;

public class GameRowActions extends UiElementObject {
    public GameRowActions(Page page) {
        super(page);
    }

    public void join(int gameId) {
        String testid = "join-button-%d".formatted(gameId);
        Locator button = page.getByTestId(testid);
        PlaywrightAssertions.assertThat(button).isVisible();
        PlaywrightAssertions.assertThat(button).isEnabled();
        button.click();
    }

    public void start(int gameId) {
        String testid = "start-button-%d".formatted(gameId);
        Locator button = page.getByTestId(testid);
        PlaywrightAssertions.assertThat(button).isVisible();
        PlaywrightAssertions.assertThat(button).isEnabled();
        button.click();
    }

    public void goTo(Integer gameId) {
        String testid = "goto-button-%d".formatted(gameId);
        Locator button = page.getByTestId(testid);
        PlaywrightAssertions.assertThat(button).isVisible();
        PlaywrightAssertions.assertThat(button).isEnabled();
        button.click();
    }


    public void delete(int gameId) {
        String testid = "delete-button-%d".formatted(gameId);
        Locator button = page.getByTestId(testid);
        PlaywrightAssertions.assertThat(button).isVisible();
        PlaywrightAssertions.assertThat(button).isEnabled();
        button.click();
    }
}
