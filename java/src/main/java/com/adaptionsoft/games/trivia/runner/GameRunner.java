
package com.adaptionsoft.games.trivia.runner;

import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.GameFactory;


public class GameRunner {
    public static void main(String[] args) {
        Game game = GameFactory.createDefault( "Chet", "Pat", "Sue", "Joe", "Vlad");
        game.play();
    }
}
