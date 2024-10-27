package com.adaptionsoft.games.trivia.game.web;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;

class CreateGameRequestDtoTest {

    @Test
    void withTrimmedInputs() {
        // GIVEN
        CreateGameRequestDto requestDto = new CreateGameRequestDto("  \n  \t gameName  \n  \t ",
                new UserDto("userId", "  \n  \t userName  \n  \t "));
        // WHEN
        CreateGameRequestDto requestDtoWithTrimmedInputs = requestDto.withTrimmedInputs();

        // THEN
        assertSoftly(sa -> {
            sa.assertThat(requestDtoWithTrimmedInputs)
                    .extracting(CreateGameRequestDto::gameName)
                    .isEqualTo("gameName");
            sa.assertThat(requestDtoWithTrimmedInputs)
                    .extracting(CreateGameRequestDto::creator)
                    .extracting(UserDto::name)
                    .isEqualTo("userName");
        });
    }
}
