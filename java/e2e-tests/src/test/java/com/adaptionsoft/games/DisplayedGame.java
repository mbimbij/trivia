package com.adaptionsoft.games;

record DisplayedGame(
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
