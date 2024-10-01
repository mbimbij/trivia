package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class GameRowActions extends UiElementObject {
    public GameRowActions(Page page) {
        super(page);
    }

    public void clickJoinButton(int gameId) {
        String testid = "join-button-%d".formatted(gameId);
        clickButtonByTestid(testid);
    }

    public void start(int gameId) {
        String testid = "start-button-%d".formatted(gameId);
        clickButtonByTestid(testid);
    }

    public void goTo(Integer gameId) {
        String testid = "goto-button-%d".formatted(gameId);
        clickButtonByTestid(testid);
    }


    public void delete(int gameId) {
        String testid = "delete-button-%d".formatted(gameId);
        clickButtonByTestid(testid);
    }

    public String getGameDetailsLink(int gameId) {
        Locator locator = page.getByTestId("game-details-%d".formatted(gameId));
        return locator.getAttribute("href");
    }

    public void clickGameDetailsLink(int gameId) {
        clickElementByTestid("game-details-%d".formatted(gameId));
    }
}
