package com.adaptionsoft.games.utils;

import com.microsoft.playwright.Playwright;

public class PlaywrightSingleton {
    private static Playwright instance = null;

    public static Playwright getInstance() {
        if(instance == null){
            instance = Playwright.create();
        }
        return instance;
    }
}
