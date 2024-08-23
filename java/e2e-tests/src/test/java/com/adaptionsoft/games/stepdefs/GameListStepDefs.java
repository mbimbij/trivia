package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.*;
import com.adaptionsoft.games.domain.pageObjects.*;
import com.adaptionsoft.games.domain.views.DisplayedGame;
import com.adaptionsoft.games.trivia.game.web.GameResponseDto;
import com.adaptionsoft.games.utils.TestUtils;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Slf4j
@RequiredArgsConstructor
public class GameListStepDefs {

    private final TestContext testContext;
    private final GamesListPage gamesListPage;
    private final GameRowActions gameRowActions;
    private final CreateGameUiElement createGameUiElement;
    private final Backend backend;
    private final Janitor janitor;

    @Then("qa-user sees the following games, filtered for creators \"{strings}\"")
    public void theFollowingGamesAreDisplayedForUsers(Collection<String> userNames, Collection<DisplayedGame> expected) {
        await().atMost(Duration.ofSeconds(5))
                .pollInterval(TestUtils.pollInterval)
                .untilAsserted(
                        () -> {
                            List<DisplayedGame> actual = gamesListPage.getDisplayedGamesForUsers(userNames);
                            assertThat(actual).isEqualTo(expected);
                        }
                );
    }

    @When("{actor} creates a game named {string} from the backend")
    public void testUserCreatesAGameNamed(Actor actor, String gameName) {
        GameResponseDto gameResponseDto = backend.createGame(gameName, actor.toUserDto());
        janitor.disablePlayersShuffling(gameResponseDto.id());
        testContext.putGameId(gameName, Objects.requireNonNull(gameResponseDto).id());
    }

    @When("{actor} creates a game named {string} from the frontend")
    public void qaUserCreatesAGameNamed(Actor actor, String gameName) {
        int createdGameId = createGameUiElement.createGame(gameName);
        janitor.disablePlayersShuffling(createdGameId);
        testContext.putGameId(gameName, createdGameId);
    }

    @When("{string} deletes {string} from the backend")
    public void deletesTheGameNamed(String userName, String gameName) {
        Integer gameId = testContext.getGameIdForName(gameName);
        backend.deleteGame(gameId);
        testContext.removeGameId(gameName);
    }

    @When("qa-user deletes {string} from the frontend")
    public void deletesFromTheFrontend(String gameName) {
        Integer gameId = testContext.getGameIdForName(gameName);
        gameRowActions.delete(gameId);
    }
}
