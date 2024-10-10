package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Page;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class AuthenticationPage extends PageWithStaticUrl {

    private final GamesListPage gamesListPage;
    private static final String EMAIL_PASSWORD_IDP_BUTTON = ".firebaseui-idp-password";
    private static final String EMAIL_INPUT = "#ui-sign-in-email-input";
    private static final String PASSWORD_INPUT = "#ui-sign-in-password-input";
    private static final String SUBMIT_BUTTON = ".firebaseui-id-submit";

    public AuthenticationPage(String basePath, Page page, GamesListPage gamesListPage) {
        super(basePath + "/authentication", page);
        this.gamesListPage = gamesListPage;
    }

    @SneakyThrows
    public void loginViaEmailAndPassword(String email, String password) {
        log.info("logging in");
        page.navigate(this.url);
        page.locator("css=" + EMAIL_PASSWORD_IDP_BUTTON).click();
        page.locator("css=" + EMAIL_INPUT).fill(email);
//        page.screenshot(new Page.ScreenshotOptions()
//                .setPath(Path.of("screenshots/screenshot_fill_email"))
//                .setFullPage(true)
//        );
        page.locator("css=" + SUBMIT_BUTTON).click();
        Thread.sleep(2000);
        page.locator("css=" + PASSWORD_INPUT).fill(password);
        gamesListPage.executeAndWaitForWebSocketMessages(() -> {
            page.locator("css=" + SUBMIT_BUTTON).click();
            gamesListPage.waitForUrl();
        });
        log.info("logged in");
    }
}
