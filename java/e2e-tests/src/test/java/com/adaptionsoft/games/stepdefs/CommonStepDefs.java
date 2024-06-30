package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.*;
import com.adaptionsoft.games.domain.views.DisplayedGame;
import com.adaptionsoft.games.utils.PlaywrightSingleton;
import com.adaptionsoft.games.utils.TestUtils;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class CommonStepDefs {
    private final FrontendActor qaFrontendActor;
    private final TestRunnerActor testRunnerActor;
    private final TestContext testContext;
    private final ActorService actorService;
    private final BackendActor backendActor1;
    private final BackendActor qaBackendActor;

    public CommonStepDefs(FrontendActor qaFrontendActor,
                          TestRunnerActor testRunnerActor,
                          TestContext testContext,
                          ActorService actorService,
                          BackendActor backendActor1,
                          BackendActor qaBackendActor) {
        this.qaFrontendActor = qaFrontendActor;
        this.testRunnerActor = testRunnerActor;
        this.testContext = testContext;
        this.actorService = actorService;
        this.backendActor1 = backendActor1;
        this.qaBackendActor = qaBackendActor;
    }

    @Before
    public void setUp() {
        qaFrontendActor.clearConsoleLogs();
    }

    @After
    public void tearDown() {
        testRunnerActor.deleteTestGames();
    }

    @AfterAll
    public static void afterAll() {
        PlaywrightSingleton.getInstance().close();
    }

    @Given("2 existing games")
    public void games() {
        backendActor1.createGame(TestContext.TEST_GAME_NAME_1);
        qaBackendActor.createGame(TestContext.TEST_GAME_NAME_2);
    }

    @Then("the following games are displayed")
    public void theFollowingGamesAreDisplayed(Collection<DisplayedGame> expected) {
        await().atMost(Duration.ofSeconds(5))
                .pollInterval(TestUtils.pollInterval)
                .untilAsserted(
                        () -> assertThat(qaFrontendActor.getDisplayedGamesForUsers(emptyList())).isEqualTo(expected)
                );
    }

    @When("{string} joins {string}")
    public void userJoinsGame(String userName, String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        TestActor testActor = actorService.getActorByName(userName);
        testActor.join(gameId);
    }

    @And("no error is displayed in the console")
    public void noErrorIsDisplayedInTheConsole() {
        List<String> errorLogs = qaFrontendActor.getErrorLogs();
        String errorLogsString = String.join("\n", errorLogs);
        String failMessage = "The following error logs were present%n%s".formatted(errorLogsString);
        assertThat(errorLogs)
                .withFailMessage(() -> failMessage)
                .isEmpty();
    }

    @When("{string} starts {string}")
    public void testUserStartsTestGame(String userName, String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        TestActor testActor = actorService.getActorByName(userName);
        testActor.start(gameId);
    }

    @Given("previous test data cleared")
    public void previousTestDataCleared() {
        testRunnerActor.deleteTestGames();
    }
}
