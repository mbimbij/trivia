package com.adaptionsoft.games.trivia;


import com.adaptionsoft.games.trivia.runner.GameRunner;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SomeTest {

    @Test
    public void should_not_differ_from_golden() throws IOException {
        // GIVEN
        int seed = 0;
        System.setOut(new PrintStream("src/test/resources/lead.txt"));
        String gold = Files.readString(Paths.get("src/test/resources/gold.txt"));

        // WHEN
        new GameRunner(seed).doRun();

        // THEN
        String lead = Files.readString(Paths.get("src/test/resources/lead.txt"));
        assertEquals(gold, lead);
    }
}
