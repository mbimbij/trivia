package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.adaptionsoft.games.trivia.web.UserDto;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import jakarta.validation.constraints.NotBlank;

public class QaPlayer extends TestPlayer {
    private final Page page;

    QaPlayer(@NotBlank String id, @NotBlank String name, Page page) {
        super(id, name);
        this.page = page;
    }

    public static QaPlayer from(UserDto userDto, Page page) {
        return new QaPlayer(userDto.id(), userDto.name(), page);
    }

    @Override
    void join(GameResponseDto game) {
        clickOnButtonForGame(game.id(), "join");
    }

    @Override
    void start(GameResponseDto game) {
        clickOnButtonForGame(game.id(), "start");
    }

    private void clickOnButtonForGame(Integer gameId, String buttonName) {
        Locator button = page.getByTestId("%s-button-%d".formatted(buttonName, gameId));
        PlaywrightAssertions.assertThat(button).isVisible();
        button.click();
    }
}
