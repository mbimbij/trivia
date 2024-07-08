package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Page;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class UiElementObjectWithUrl extends UiElementObject {
    protected final String url;

    public UiElementObjectWithUrl(String url, Page page) {
        super(page);
        this.url = url;
    }

    public void navigateTo() {
        log.info("Navigating to %s".formatted(url));
        page.navigate(url);
    }

    public void waitForUrl() {
        log.info("Waiting for url %s".formatted(url));
        page.waitForURL(url);
    }
}
