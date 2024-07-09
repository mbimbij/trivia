package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.Janitor;
import com.adaptionsoft.games.domain.pageObjects.Console;
import com.adaptionsoft.games.utils.PlaywrightSingleton;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Hooks {

    private final Janitor janitor;
    private final Console console;

    @Before
    public void setUp() {
        console.clearLogs();
    }

    @After
    public void tearDown() {
        janitor.deleteTestGames();
    }

    @AfterAll
    public static void afterAll() {
        PlaywrightSingleton.getInstance().close();
    }
}
