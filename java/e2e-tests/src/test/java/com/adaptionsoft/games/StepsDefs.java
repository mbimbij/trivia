package com.adaptionsoft.games;

import com.adaptionsoft.games.trivia.web.CreateGameRequestDto;
import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.adaptionsoft.games.trivia.web.UserDto;
import com.microsoft.playwright.*;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class StepsDefs {

    private static Browser browser;
    private static Page page;
    private final UserDto user1 = new UserDto("id-user1", "user1");
    private final UserDto user2 = new UserDto("id-user2", "user2");

    private static Playwright playwright;

    private RestTemplate restTemplate = new RestTemplate();

    @BeforeAll
    public static void setUp() throws Exception {
        playwright = Playwright.create();
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000);
        browser = playwright.firefox().launch(launchOptions);
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();
        BrowserContext newContext = browser.newContext(contextOptions);
        page = newContext.newPage();
    }

    @AfterAll
    public static void afterAll() {
        playwright.close();
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

    @Given("{int} existing games")
    public void games(int arg0) {
        CreateGameRequestDto createGameRequest1 = new CreateGameRequestDto("game1", user1);
        CreateGameRequestDto createGameRequest2 = new CreateGameRequestDto("game2", user1);

        ResponseEntity<GameResponseDto> responseEntity1 = restTemplate.postForEntity("http://localhost:8080/games", createGameRequest1, GameResponseDto.class);
        assertThat(responseEntity1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        GameResponseDto game1 = responseEntity1.getBody();

        ResponseEntity<GameResponseDto> responseEntity2 = restTemplate.postForEntity("http://localhost:8080/games", createGameRequest2, GameResponseDto.class);
        assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        GameResponseDto game2 = responseEntity2.getBody();

        System.out.println();
    }

    @When("test user accesses games list page")
    public void accessingGamesList() {
        page.navigate("http://localhost:4200/games");
    }

    @Then("the games are displayed")
    public void theGamesAreDisplayed() {
    }
}
