package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PageWithDynamicUrl extends PageWithUrl {

    public PageWithDynamicUrl(String url, Page page) {
        super(url, page);
    }

    public void navigateTo(int gameId) {
        String resolvedUrl = getResolvedUrl(gameId);
        log.info("Navigating to %s".formatted(resolvedUrl));
        page.navigate(resolvedUrl);
    }

    public void waitForUrl(int gameId) {
        String resolvedUrl = getResolvedUrl(gameId);
        log.info("Waiting for url %s".formatted(resolvedUrl));
        page.waitForURL(resolvedUrl);
    }

    protected String getResolvedUrl(int gameId) {
        return url.formatted(gameId);
    }
}
