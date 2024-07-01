package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.BackendActor;
import com.adaptionsoft.games.domain.FrontendActor;
import com.adaptionsoft.games.domain.views.DisplayedGame;
import com.adaptionsoft.games.utils.TestUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Slf4j
public class GameListStepDefs {

    private final FrontendActor qaFrontendActor;
    private final BackendActor qaBackendActor;
    private final BackendActor backendActor1;

    public GameListStepDefs(FrontendActor qaFrontendActor,
                            @Qualifier("qaBackendActor") BackendActor qaBackendActor,
                            @Qualifier("backendActor1") BackendActor backendActor1) {
        this.qaFrontendActor = qaFrontendActor;
        this.qaBackendActor = qaBackendActor;
        this.backendActor1 = backendActor1;
    }

    @Given("a logged-in test user on the game-list page")
    public void logged_in_test_user_on_game_list_page() {
        if(!qaFrontendActor.isLoggedIn()){
            qaFrontendActor.login();
        } else {
            qaFrontendActor.gotoGamesPageByUrl();
        }
    }

    @Then("qa-user sees the following games, filtered for creators \"{strings}\"")
    public void theFollowingGamesAreDisplayedForUsers(Collection<String> userNames, Collection<DisplayedGame> expected) {
        await().atMost(Duration.ofSeconds(5))
                .pollInterval(TestUtils.pollInterval)
                .untilAsserted(
                        () -> {
                            List<DisplayedGame> actual = qaFrontendActor.getDisplayedGamesForUsers(userNames);
                            assertThat(actual).isEqualTo(expected);
                        }
                );
    }

    @When("test-user-1 creates a game named {string}")
    public void testUserCreatesAGameNamed(String gameName) {
        backendActor1.createGame(gameName);
    }

    @When("qa-user creates a game named {string}")
    public void qaUserCreatesAGameNamed(String gameName) {
        qaBackendActor.createGame(gameName);
    }

    @When("test-user-1 deletes the game named {string}")
    public void deletesTheGameNamed(String gameName) {
        backendActor1.deleteGame(gameName);
    }

    @When("qa-user clicks on {string} button for {string}")
    public void qaUserClicksOnButtonForGame(String buttonName, String gameName) {
        qaFrontendActor.clickOnButtonForGame_GameList(buttonName, gameName);
    }
}
