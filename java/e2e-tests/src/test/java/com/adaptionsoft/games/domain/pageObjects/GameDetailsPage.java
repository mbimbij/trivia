package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Page;

public class GameDetailsPage extends PageWithDynamicUrl {

    public GameDetailsPage(Page page, String urlTemplate) {
        super(urlTemplate, page);
    }

}