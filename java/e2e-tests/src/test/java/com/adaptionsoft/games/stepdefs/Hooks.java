package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.Janitor;
import com.adaptionsoft.games.domain.pageObjects.Console;
import com.adaptionsoft.games.domain.pageObjects.HealthPage;
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
    private final HealthPage healthPage;
    private final Console console;
    private boolean noErrorLogsExpectedInConsole;

    @Before
    public void setUp() {
        janitor.verifyBackendIsUp();
        healthPage.verifyFrontendIsUp();
        console.clearLogs();
        noErrorLogsExpectedInConsole = true;
        janitor.resetGetGameByIdMethodBehaviour();
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

    @And("error logs are expected in the console")
    public void logsAreExpectedInTheConsole() {
        noErrorLogsExpectedInConsole = false;
    }
}
