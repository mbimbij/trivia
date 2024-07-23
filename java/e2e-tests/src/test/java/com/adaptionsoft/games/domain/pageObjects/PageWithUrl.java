package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Page;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class PageWithUrl extends UiElementObject {
    protected final String url;

    public PageWithUrl(String url, Page page) {
        super(page);
        this.url = url;
    }

    public void reload() {
        page.reload();
    }
}
