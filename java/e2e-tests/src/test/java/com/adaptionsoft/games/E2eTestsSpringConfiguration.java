package com.adaptionsoft.games;

import com.adaptionsoft.games.domain.ActorService;
import com.adaptionsoft.games.domain.Janitor;
import com.adaptionsoft.games.domain.TestContext;
import com.adaptionsoft.games.domain.TestProperties;
import com.adaptionsoft.games.domain.pageObjects.*;
import com.adaptionsoft.games.trivia.game.web.WebConfig;
import com.adaptionsoft.games.utils.PlaywrightSingleton;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@ComponentScan
@EnableConfigurationProperties(TestProperties.class)
@Import(WebConfig.class)
public class E2eTestsSpringConfiguration {
    @Bean
    public TestContext testContext() {
        return new TestContext();
    }

    @Bean
    public Playwright playwright() {
        return PlaywrightSingleton.getInstance();
    }

    @Bean
    public Page page(Playwright playwright) {
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
//                .setHeadless(false)
                .setHeadless(true)
//                .setSlowMo(1000)
                ;
        Browser browser = playwright.chromium().launch(launchOptions);
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
//                .setRecordVideoDir(Path.of("videos"))
//                .setRecordVideoSize(1280, 1024)
                ;
        BrowserContext newContext = browser.newContext(contextOptions);
        return newContext.newPage();
    }

    @Bean
    public Console consoleLogs(Page page) {
        return new Console(page);
    }

    @Bean
    public Janitor testRunnerActor(RestTemplate restTemplate, TestProperties testProperties, TestContext testContext) {
        return new Janitor(restTemplate,testProperties.getBackendUrlBase(), testContext);
    }

    @Bean
    public ActorService actorService(TestProperties testProperties) {
        return new ActorService(testProperties
        );
    }

    @Bean
    public AuthenticationPage authenticationPage(TestProperties testProperties, Page page, GamesListPage gamesListPage) {
        return new AuthenticationPage(testProperties.getFrontendUrlBase(), page, gamesListPage);
    }

    @Bean
    public GamesListPage gamesListPage(TestProperties testProperties, Page page, TestContext testContext) {
        return new GamesListPage(testProperties.getFrontendUrlBase(), page, testContext);
    }

    @Bean
    public HealthPage healthPage(TestProperties testProperties, Page page) {
        return new HealthPage(testProperties.getFrontendUrlBase(), page);
    }

    @Bean
    public GameRowActions gameRowActions(Page page) {
        return new GameRowActions(page);
    }

    @Bean
    public OngoingGamePage ongoingGamePage(Page page, TestProperties testProperties) {
        String urlTemplate = testProperties.getFrontendUrlBase() + "/games/%d";
        return new OngoingGamePage(page, urlTemplate);
    }

    @Bean
    public GameDetailsPage gameDetailsPage(Page page, TestProperties testProperties) {
        String urlTemplate = testProperties.getFrontendUrlBase() + "/games/%d/details";
        return new GameDetailsPage(page, urlTemplate);
    }

    @Bean
    public CreateGameDialog createGameUiElement(Page page) {
        return new CreateGameDialog(page);
    }

    @Bean
    public JoinGameDialog joinGameDialog(Page page) {
        return new JoinGameDialog(page);
    }

    @Bean
    public RestTemplate restTemplate(@Qualifier("stateDeserializer") Module stateDeserializer) {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        converter.setObjectMapper(mapper.registerModule(stateDeserializer));
        restTemplate.getMessageConverters().add(0, converter);
        return restTemplate;
    }
    
    @Bean
    public Backend backend(RestTemplate restTemplate, TestProperties testProperties) {
        return new Backend(restTemplate,testProperties.getBackendUrlBase());
    }

    @Bean
    public RenameUser renameUser(Page page) {
        return new RenameUser(page);
    }

    @Bean
    public Navbar navbar(Page page) {
        return new Navbar(page);
    }
}
