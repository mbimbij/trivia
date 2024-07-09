package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Page;
import lombok.Getter;

@Getter
public class UiElementObject {
    protected final Page page;

    public UiElementObject(Page page) {
        this.page = page;
    }
}
