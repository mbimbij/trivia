package com.adaptionsoft.games.domain.pageObjects;

import com.adaptionsoft.games.domain.views.DisplayedGame;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GamesListPage extends PageWithStaticUrl {
    public GamesListPage(String basePath, Page page) {
        super(basePath + "/games", page);
    }

    /**
     * @param userNames Pass null or an empty collection to get games for all users
     * @return
     */
    public List<DisplayedGame> getDisplayedGamesForUsers(Collection<String> userNames) {
        Predicate<ElementHandle> predicate = (userNames == null || userNames.isEmpty())
                ? h -> true
                : h -> userNames.contains(h.querySelector(".creator-name").textContent().trim());
        return page.querySelectorAll(".game-row").stream()
                .filter(predicate)
                .map(this::convertToObject)
                .collect(Collectors.toList());
    }

    public DisplayedGame convertToObject(ElementHandle elementHandle) {
        return new DisplayedGame(
                elementHandle.querySelector(".name").textContent().trim(),
                elementHandle.querySelector(".creator-name").textContent().trim(),
                elementHandle.querySelector(".players-names").textContent().trim(),
                elementHandle.querySelector(".state").textContent().trim(),
                getButtonState(elementHandle, "start"),
                getButtonState(elementHandle, "join"),
                elementHandle.querySelector(".join").textContent().trim(),
                getButtonState(elementHandle, "goto"),
                getButtonState(elementHandle, "delete")
        );
    }

    public Boolean getButtonState(ElementHandle elementHandle, String buttonName) {
        return Optional.ofNullable(elementHandle.querySelector("." + buttonName + " button")).map(ElementHandle::isEnabled).orElse(null);
    }
}
