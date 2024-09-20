package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Page;
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
        page.getByTestId(testid).click();
    }

    public void clickElementByTestid(int gameId, Page page) {
        page.getByTestId("game-details-%d".formatted(gameId)).click();
    }
}
