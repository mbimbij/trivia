package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.*;
import com.adaptionsoft.games.domain.pageObjects.*;
import com.adaptionsoft.games.domain.views.DisplayedGame;
import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.adaptionsoft.games.utils.TestUtils;
import com.microsoft.playwright.Page;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Collection;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Slf4j
@RequiredArgsConstructor
public class CommonStepDefs {
    private final Janitor janitor;
    private final TestContext testContext;
    private final ActorService actorService;
    private final AuthenticationPage authenticationPage;
    private final GamesListPage gamesListPage;
    private final GameRowActions gameDetailsPage;
    private final Backend backend;
    private final TestProperties testProperties;
    private final Page page;

    @Given("2 existing games")
    public void games() {
        Actor backendActor1 = actorService.getActorByLookupName(TestProperties.TEST_USER_NAME_1);
        Actor qaBackendActor = actorService.getActorByLookupName(TestProperties.QA_BACKEND_LOOKUP_NAME);
        GameResponseDto gameResponseDto = backend.createGame(TestProperties.TEST_GAME_NAME_1, backendActor1.toUserDto());
        testContext.putGameId(TestProperties.TEST_GAME_NAME_1, Objects.requireNonNull(gameResponseDto).id());
        GameResponseDto gameResponseDto2 = backend.createGame(TestProperties.TEST_GAME_NAME_2, qaBackendActor.toUserDto());
        testContext.putGameId(TestProperties.TEST_GAME_NAME_2, Objects.requireNonNull(gameResponseDto2).id());
    }

    @SneakyThrows
    @Given("{actor} on the game-list page")
    public void logged_in_test_user_on_game_list_page(Actor qaActor) {
        if(!qaActor.isLoggedIn()){
            authenticationPage.loginViaEmailAndPassword(testProperties.getQaUserEmail(), testProperties.getQaUserPassword());
            qaActor.setLoggedIn(true);
            // TODO find a better way to wait for websocket connection for game state update
            Thread.sleep(1000);
        }else {
            gamesListPage.navigateTo();
        }
    }

    @Then("the following games are displayed")
    public void theFollowingGamesAreDisplayed(Collection<DisplayedGame> expected) {
        await().atMost(Duration.ofSeconds(5))
                .pollInterval(TestUtils.pollInterval)
                .untilAsserted(
                        () -> assertThat(gamesListPage.getDisplayedGamesForUsers(emptyList()))
                                .isEqualTo(expected)
                );
    }

    @When("{actor} joins {string} from the backend")
    public void userJoinsGameFromTheBackend(Actor actor, String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        backend.joinGame(gameId, actor.toUserDto());
    }

    @When("{actor} starts {string} from the backend")
    public void testUserStartsTestGame(Actor actor, String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        backend.startGame(gameId, actor.toUserDto().id());
    }

    @When("{actor} starts {string} from the frontend")
    public void testUserStartsTestGameFromTheFrontend(Actor actor, String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        gameDetailsPage.start(gameId);
    }

    @Given("previous test data cleared")
    public void previousTestDataCleared() {
        janitor.deleteTestGames();
    }

    @When("qa-user reloads the page")
    public void qaUserReloadsThePage() {
        page.reload();
    }
}
