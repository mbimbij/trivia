
package com.adaptionsoft.games.trivia.runner;

import com.adaptionsoft.games.uglytrivia.Game;


public class GameRunner {
    public static void main(String[] args) {
        Game game = new Game( "Chet", "Pat", "Sue", "Joe", "Vlad");
        game.play();
    }
}
