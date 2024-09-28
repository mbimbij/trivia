package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Page;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class PageWithStaticUrl extends PageWithUrl {

    public PageWithStaticUrl(String url, Page page) {
        super(url, page);
    }

    public void navigateTo() {
        log.info("Navigating to %s".formatted(url));
        page.navigate(url);
        page.waitForLoadState();
    }

    public void waitForUrl() {
        log.info("Waiting for url %s".formatted(url));
        page.waitForURL(url);
        page.waitForLoadState();
    }
}
