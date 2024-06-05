package com.adaptionsoft.games;

import com.adaptionsoft.games.trivia.web.CreateGameRequestDto;
import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.adaptionsoft.games.trivia.web.UserDto;
import com.microsoft.playwright.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class StepsDefs {

    private static Playwright playwright;
    private static Browser browser;
    private static Page page;

    private final RestTemplate restTemplate = new RestTemplate();

    private final UserDto testUser1 = new UserDto("id-test-user-1", "test-user-1");
    private final UserDto testUser2 = new UserDto("id-test-user-2", "test-user-2");
    private final String gameName1 = "test-game-1";
    private final String gameName2 = "test-game-2";

    private GameResponseDto game1;
    private GameResponseDto game2;

    @BeforeAll
    public static void beforeAll() throws Exception {
        playwright = Playwright.create();
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(false).setSlowMo(1000)
                ;
        browser = playwright.firefox().launch(launchOptions);
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();
        BrowserContext newContext = browser.newContext(contextOptions);
        page = newContext.newPage();
    }

    @AfterAll
    public static void afterAll() {
        playwright.close();
    }

    @After
    public void tearDown() {
        if(this.game1 != null) {
            restTemplate.delete("http://localhost:8080/games/{gameId}", this.game1.id());
        }
        if(this.game2 != null) {
            restTemplate.delete("http://localhost:8080/games/{gameId}", this.game2.id());
        }
    }

    @Given("a logged-in test user")
    public void logged_in_test_user() {
        page.navigate("http://localhost:4200/authentication");
        page.querySelector(".firebaseui-idp-password").click();
        page.querySelector("#ui-sign-in-email-input").fill("joseph.mbimbi+test@gmail.com");
        page.querySelector(".firebaseui-id-submit").click();
        page.querySelector("#ui-sign-in-password-input").fill("azerty1!!");
        page.querySelector(".firebaseui-id-submit").click();
    }

    @Given("a test user")
    public void anTestUser() {
    }

    @Given("{int} existing games")
    public void games(int arg0) {
        CreateGameRequestDto createGameRequest1 = new CreateGameRequestDto(gameName1, testUser1);
        CreateGameRequestDto createGameRequest2 = new CreateGameRequestDto(gameName2, testUser1);

        ResponseEntity<GameResponseDto> responseEntity1 = restTemplate.postForEntity("http://localhost:8080/games", createGameRequest1, GameResponseDto.class);
        assertThat(responseEntity1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        game1 = responseEntity1.getBody();

        ResponseEntity<GameResponseDto> responseEntity2 = restTemplate.postForEntity("http://localhost:8080/games", createGameRequest2, GameResponseDto.class);
        assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        game2 = responseEntity2.getBody();

        System.out.println();
    }

    @When("test user accesses games list page")
    public void accessingGamesList() {
        page.navigate("http://localhost:4200/games");
    }

    @Then("the following games are displayed")
    public void theFollowingGamesAreDisplayed(Collection<DisplayedGame> expectedDisplayedGames) {
        List<DisplayedGame> actualDisplayedGames = page.querySelectorAll(".game-row").stream()
                .filter(h -> Objects.equals(h.querySelector(".creator-name").textContent(),testUser1.name() ))
                .map(this::convertElementToObject)
                .toList();

        assertThat(actualDisplayedGames).isEqualTo(expectedDisplayedGames);
    }

    public DisplayedGame convertElementToObject(ElementHandle elementHandle) {
        return new DisplayedGame(
                elementHandle.querySelector(".name").textContent(),
                elementHandle.querySelector(".creator-name").textContent(),
                elementHandle.querySelector(".players-names").textContent(),
                elementHandle.querySelector(".state").textContent(),
                getButtonState(elementHandle, "start"),
                getButtonState(elementHandle, "join"),
                getButtonState(elementHandle, "goto"),
                getButtonState(elementHandle, "delete")
                );
    }

    private static Boolean getButtonState(ElementHandle elementHandle, String buttonName) {
        return Optional.ofNullable(elementHandle.querySelector("." + buttonName + " button")).map(ElementHandle::isEnabled).orElse(null);
    }

    @DataTableType
    public Collection<DisplayedGame> displayedGames(DataTable dataTable) {
        Collection<DisplayedGame> displayedGames = TestUtils.convertDatatableList(dataTable, DisplayedGame.class);
        return displayedGames;

    }

    private record DisplayedGame(
            String name,
            String creator,
            String players,
            String state,
            Boolean startEnabled,
            Boolean joinEnabled,
            Boolean gotoEnabled,
            Boolean deleteEnabled
    ) {
    }
}
