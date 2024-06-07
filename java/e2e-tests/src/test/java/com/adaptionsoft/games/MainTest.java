package com.adaptionsoft.games;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


class MainTest {

    @SneakyThrows
    @Test
    void getting_started() {
        try(Playwright playwright = Playwright.create();) {
//            Playwright playwright = Playwright.create();
            Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
            BrowserContext newContext = browser.newContext(
                    new Browser.NewContextOptions().setRecordVideoDir(Paths.get("videos")).setRecordVideoSize(1280, 720));
            Page page = newContext.newPage();

            page.navigate("https://playwright.dev/");

            assertThat(page).hasTitle(Pattern.compile("Playwright"));

            Locator getStarted = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get Started"));

            assertThat(getStarted).hasAttribute("href", "/docs/intro");

            getStarted.click();
            assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Installation")))
                    .isVisible();
            page.close();
            newContext.close();
            browser.close();
            playwright.close();
        }

//        Playwright playwright = Playwright.create();
//        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
//        BrowserContext context = browser.newContext(
//                new Browser.NewContextOptions().setRecordVideoDir(Paths.get("videos/")).setRecordVideoSize(1280, 720));
//        Page page = browser.newPage();
//        page.navigate("https://playwright.dev/");
//
//        assertThat(page).hasTitle(Pattern.compile("Playwright"));
//
//        Locator getStarted = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get Started"));
//
//        assertThat(getStarted).hasAttribute("href", "/docs/intro");
//
//        getStarted.click();
//        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Installation")))
//                .isVisible();
////        page.close();
//        context.close();
////        browser.close();
//        playwright.close();

    }

//    @Test
//    void name2() {
//        Playwright playwright = Playwright.create();
//        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
//        BrowserContext newContext = browser.newContext(
//                new Browser.NewContextOptions().setRecordVideoDir(Paths.get("videos/")).setRecordVideoSize(1280, 720));
//        Page page = newContext.newPage();
//
//        page.navigate("https://www.programsbuzz.com/user/login");
//
//        page.locator("#edit-name").type("Nauto");
//        page.locator("#edit-pass").type("Madara");
//
//        newContext.close();
//        playwright.close();
//    }


//    @Test
//    void test3() {
//        try (Playwright playwright = Playwright.create()) {
//            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
//            BrowserContext context = browser.newContext(new Browser.NewContextOptions().setRecordVideoDir(Paths.get("videos")));
//            Page page = browser.newPage();
//            page.navigate("https://playwright.dev/");
//
//            assertThat(page).hasTitle(Pattern.compile("Playwright"));
//
//            Locator getStarted = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get Started"));
//
//            assertThat(getStarted).hasAttribute("href", "/docs/intro");
//
//            getStarted.click();
//            assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Installation")))
//                    .isVisible();
//            page.close();
//            context.close();
//            browser.close();
//        }
//    }
}