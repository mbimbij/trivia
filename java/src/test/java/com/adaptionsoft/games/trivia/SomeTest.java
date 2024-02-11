package com.adaptionsoft.games.trivia;


import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.GameFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SomeTest {

    @Test
    public void should_not_differ_from_golden() throws IOException {
        // GIVEN
        System.setOut(new PrintStream("src/test/resources/lead.txt"));
        String gold = Files.readString(Paths.get("src/test/resources/gold.txt"));
        int seed = 0;
        Game game = GameFactory.createWithSeed(new Random(seed), "Chet", "Pat", "Sue", "Joe", "Vlad");

        // WHEN
        game.play();

        // THEN
        String lead = Files.readString(Paths.get("src/test/resources/lead.txt"));
        assertEquals(gold, lead);
    }

}
