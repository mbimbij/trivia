
package com.adaptionsoft.games.trivia.runner;

import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.GameFactory;
import com.adaptionsoft.games.uglytrivia.PlayersFactory;
import com.adaptionsoft.games.uglytrivia.event.MockEventPublisher;


public class GameRunner {
    public static void main(String[] args) {
        MockEventPublisher eventPublisher = new MockEventPublisher();
        GameFactory gameFactory = new GameFactory(eventPublisher, new PlayersFactory(eventPublisher));
        Game game = gameFactory.create( "Chet", "Pat", "Sue", "Joe", "Vlad");
        game.play();
    }
}
