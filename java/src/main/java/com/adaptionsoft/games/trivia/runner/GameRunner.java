
package com.adaptionsoft.games.trivia.runner;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.GameFactory;
import com.adaptionsoft.games.trivia.domain.PlayersFactory;
import com.adaptionsoft.games.trivia.domain.event.MockEventPublisher;


public class GameRunner {
    public static void main(String[] args) {
        MockEventPublisher eventPublisher = new MockEventPublisher();
        GameFactory gameFactory = new GameFactory(eventPublisher, new PlayersFactory(eventPublisher));
        Game game = gameFactory.create( "Chet", "Pat", "Sue", "Joe", "Vlad");
        game.play();
    }
}
