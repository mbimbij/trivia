package com.adaptionsoft.games.trivia.shared.microarchitecture;

import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class IdTest {

    @Test
    void should_be_equal__when_appropriate() {
        StringId1 stringId11 = new StringId1("1");
        StringId1 stringId12 = new StringId1("1");
        StringId1 stringId13 = new StringId1("3");
        StringId2 stringId2 = new StringId2("1");

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(stringId11).isEqualTo(stringId12);
            softAssertions.assertThat(stringId11).isNotEqualTo(stringId13);
            softAssertions.assertThat(stringId11).isNotEqualTo(stringId2);
        });
    }

    @EqualsAndHashCode(callSuper = true)
    private static class StringId1 extends Id<String> {
        protected StringId1(String value) {
            super(value);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    private static class StringId2 extends Id<String>{
        protected StringId2(String value) {
            super(value);
        }
    }
}
