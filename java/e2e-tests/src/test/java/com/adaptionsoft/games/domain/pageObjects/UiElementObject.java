package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Locator;
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
        return page.getByTestId(testid).textContent();
    }

    public void clickButtonByTestid(String testid) {
        Locator button = page.getByTestId(testid);
        PlaywrightAssertions.assertThat(button).isVisible();
        PlaywrightAssertions.assertThat(button).isEnabled();
        button.click();
    }
}
