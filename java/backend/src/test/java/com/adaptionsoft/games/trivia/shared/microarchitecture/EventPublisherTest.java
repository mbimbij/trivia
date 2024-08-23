package com.adaptionsoft.games.trivia.shared.microarchitecture;

import com.adaptionsoft.games.trivia.game.domain.GameId;
import com.adaptionsoft.games.trivia.game.domain.event.MockEventPublisher;
import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EventPublisherTest {
    @Test
    void should_notify_listeners__when_event_published() {
        // GIVEN
        MockEventListener eventListener = new MockEventListener();
        MockEventPublisher eventPublisher = new MockEventPublisher();
        eventPublisher.register(eventListener);

        // WHEN
        eventPublisher.raise(new MockEvent());
        eventPublisher.flush();

        // THEN
        Assertions.assertThat(eventListener.isHandled()).isTrue();
    }

    public static class MockEvent extends Event{
        public MockEvent() {
            super(new GameId(0), "mock event");
        }
    }

    @Getter
    public static class MockEventListener implements EventListener {
        private boolean handled = false;

        @Override
        public boolean accept(Event event) {
            return true;
        }

        @Override
        public void handle(Event event) {
            this.handled = true;
        }
    }
}