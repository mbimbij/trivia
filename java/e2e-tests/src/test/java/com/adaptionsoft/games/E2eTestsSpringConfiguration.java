package com.adaptionsoft.games;

import com.adaptionsoft.games.domain.*;
import com.microsoft.playwright.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;

import java.nio.file.Path;

@ComponentScan
@EnableConfigurationProperties(TestProperties.class)
public class E2eTestsSpringConfiguration {
    @Bean
    public TestContext testContext(TestProperties testProperties) {
        return new TestContext(testProperties.getFrontendUrlBase(), testProperties.getBackendUrlBase());
    }

    @Bean
    public Playwright playwright() {
        return Playwright.create();
    }

    @Bean
    public Page qaFrontendActorPage(Playwright playwright) {
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(false)
//                .setSlowMo(1000)
                ;
        Browser browser = playwright.firefox().launch(launchOptions);
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setRecordVideoDir(Path.of("videos"))
                .setRecordVideoSize(640, 480)
                ;
        BrowserContext newContext = browser.newContext(contextOptions);
        return newContext.newPage();
    }

    @Bean
    public FrontendActor qaFrontendActor(TestContext testContext, Page page, TestProperties testProperties) {
        FrontendActor frontendActor = new FrontendActor(testContext.getQaUserId(),
                TestContext.QA_USER_NAME,
                page,
                testProperties.getFrontendUrlBase(),
                testProperties.getQaUserEmail(),
                testProperties.getQaUserPassword(),
                testContext);
        frontendActor.registerBrowserLogs();
        return frontendActor;
    }

    @Bean
    public TestRunnerActor testRunnerActor(TestContext testContext) {
        return new TestRunnerActor(testContext.getBackendUrlBase());
    }

    @Bean
    public BackendActor qaBackendActor(TestProperties testProperties, TestContext testContext) {
        return new BackendActor(testProperties.getQaUserId(),
                TestContext.QA_USER_NAME,
                testProperties.getBackendUrlBase(),
                testContext);
    }

    @Bean
    public BackendActor backendActor1(TestProperties testProperties, TestContext testContext) {
        return new BackendActor(TestContext.ID_TEST_USER_1,
                TestContext.TEST_USER_NAME_1,
                testProperties.getBackendUrlBase(),
                testContext);
    }

    @Bean
    public BackendActor backendActor2(TestProperties testProperties, TestContext testContext) {
        return new BackendActor(TestContext.ID_TEST_USER_2,
                TestContext.TEST_USER_NAME_2,
                testProperties.getBackendUrlBase(),
                testContext);
    }
    @EventListener
    public void handleContextRefreshEvent(ContextRefreshedEvent event) {
        System.out.println("Context Closed Event received.");
    }
}
