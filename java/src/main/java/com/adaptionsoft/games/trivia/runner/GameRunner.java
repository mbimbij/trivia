
package com.adaptionsoft.games.trivia.runner;

import com.adaptionsoft.games.uglytrivia.Game;

import java.util.Random;


public class GameRunner {


    private final Random rand;

    public GameRunner() {
        rand = new Random();
    }

    public GameRunner(int seed) {
        rand = new Random(seed);
    }

    public static void main(String[] args) {
        new GameRunner().doRun();
    }

    public void doRun() {
        new Game(rand, "Chet", "Pat", "Sue", "Joe", "Vlad").play();
    }

}
