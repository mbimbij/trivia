package com.adaptionsoft.games.domain.views;

public record DisplayedGame(
        String name,
        String creator,
        String players,
        String state,
        Boolean joinEnabled,
        String joinText,
        Boolean startEnabled,
        Boolean gotoEnabled,
        Boolean deleteEnabled
) {
}
