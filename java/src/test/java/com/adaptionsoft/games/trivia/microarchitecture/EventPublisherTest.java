package com.adaptionsoft.games.trivia.microarchitecture;

import com.adaptionsoft.games.trivia.domain.event.Event;
import com.adaptionsoft.games.trivia.domain.event.MockEventPublisher;
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
        eventPublisher.publish(new MockEvent());

        // THEN
        Assertions.assertThat(eventListener.isHandled()).isTrue();
    }

    public static class MockEvent extends Event{
        public MockEvent() {
            super("mock event");
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