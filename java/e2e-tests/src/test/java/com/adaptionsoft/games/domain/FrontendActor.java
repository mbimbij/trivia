package com.adaptionsoft.games.domain;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FrontendActor extends Actor {
    private final Page page;

    public FrontendActor(@NotBlank String id,
                         @NotBlank String name,
                         Page page) {
        super(id, name);
        this.page = page;
    }

    // TODO ajouter un test de création de partie depuis le frontend
    public void createGame(String gameName) {
        page.getByTestId("create-game-name").fill(gameName);
        Locator button = page.getByTestId("create-game-validate");
        PlaywrightAssertions.assertThat(button).isVisible();
        PlaywrightAssertions.assertThat(button).isEnabled();
        button.click();
    }
}
