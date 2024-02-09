
package com.adaptionsoft.games.trivia.runner;
import java.util.Random;

import com.adaptionsoft.games.uglytrivia.Game;


public class GameRunner {

	private static boolean notAWinner;
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
		Game aGame = new Game();

		aGame.add("Chet");
		aGame.add("Pat");
		aGame.add("Sue");
		aGame.add("Joe");
		aGame.add("Vlad");

		do {

			aGame.roll(getDiceRoll(rand));

			if (rand.nextInt(9) == 7) {
				notAWinner = aGame.wrongAnswer();
			} else {
				notAWinner = aGame.wasCorrectlyAnswered();
			}



		} while (notAWinner);
	}

	private static int getDiceRoll(Random rand) {
		return rand.nextInt(5) + 1;
	}
}
