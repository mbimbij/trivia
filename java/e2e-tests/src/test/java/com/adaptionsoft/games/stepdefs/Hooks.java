package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.Janitor;
import com.adaptionsoft.games.domain.pageObjects.Console;
import com.adaptionsoft.games.utils.PlaywrightSingleton;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class Hooks {

    private final Janitor janitor;
    private final Console console;
    private boolean noErrorLogsExpectedInConsole;

    @Before
    public void setUp() {
        console.clearLogs();
        noErrorLogsExpectedInConsole = true;
    }

    @After
    public void tearDown() {
        if(noErrorLogsExpectedInConsole){
            verifyNoErrorIsDisplayedInTheConsole();
        }
        janitor.deleteTestGames();
    }

    public void verifyNoErrorIsDisplayedInTheConsole() {
        List<String> errorLogs = console.getErrorLogs();
        String errorLogsString = String.join("\n", errorLogs);
        String failMessage = "The following error logs were present%n%s".formatted(errorLogsString);
        assertThat(errorLogs)
                .withFailMessage(() -> failMessage)
                .isEmpty();
    }

    @AfterAll
    public static void afterAll() {
        PlaywrightSingleton.getInstance().close();
    }


    @And("error logs are expected in the console")
    public void logsAreExpectedInTheConsole() {
        noErrorLogsExpectedInConsole = false;
    }
}
