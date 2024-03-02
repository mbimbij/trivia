
package com.adaptionsoft.games.trivia.runner;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.GameFactory;
import com.adaptionsoft.games.trivia.domain.event.MockEventPublisher;
import com.adaptionsoft.games.trivia.infra.EventConsoleLogger;


public class GameRunner {
    public static void main(String[] args) {
        MockEventPublisher eventPublisher = new MockEventPublisher();
        eventPublisher.register(new EventConsoleLogger());
        GameFactory gameFactory = new GameFactory(eventPublisher);
        Game game = gameFactory.create( "Chet", "Pat", "Sue", "Joe", "Vlad");
        game.play();
    }
}
