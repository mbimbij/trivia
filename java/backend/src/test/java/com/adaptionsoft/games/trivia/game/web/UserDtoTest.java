package com.adaptionsoft.games.trivia.game.web;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class UserDtoTest {

    @Test
    void withTrimmedName() {
        // GIVEN
        UserDto userDto = new UserDto("someId", " \n someName\t  ");

        // WHEN
        UserDto trimmedUserDto = userDto.withTrimmedName();

        // THEN
        assertSoftly(sa -> {
            sa.assertThat(trimmedUserDto).extracting(UserDto::id).isEqualTo("someId");
            sa.assertThat(trimmedUserDto).extracting(UserDto::name).isEqualTo("someName");
        });
    }
}
