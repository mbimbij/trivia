
package com.adaptionsoft.games.trivia.runner;

import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.GameFactory;
import com.adaptionsoft.games.uglytrivia.event.MockEventPublisher;


public class GameRunner {
    public static void main(String[] args) {
        GameFactory gameFactory = new GameFactory(new MockEventPublisher());
        Game game = gameFactory.create( "Chet", "Pat", "Sue", "Joe", "Vlad");
        game.play();
    }
}
