package com.adaptionsoft.games.domain.views;

public record DisplayedGame(
        String name,
        String creator,
        String players,
        String state,
        Boolean startEnabled,
        Boolean joinEnabled,
        String joinText,
        Boolean gotoEnabled,
        Boolean deleteEnabled
) {
}
