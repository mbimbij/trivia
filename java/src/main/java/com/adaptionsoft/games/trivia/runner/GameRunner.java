
package com.adaptionsoft.games.trivia.runner;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.GameFactory;
import com.adaptionsoft.games.trivia.domain.QuestionsLoader;
import com.adaptionsoft.games.trivia.domain.event.ObserverBasedEventPublisher;
import com.adaptionsoft.games.trivia.infra.EventConsoleLogger;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;


public class GameRunner {
    public static void main(String[] args) {
        EventPublisher eventPublisher = new ObserverBasedEventPublisher();
        eventPublisher.register(new EventConsoleLogger());
        QuestionsLoader questionsLoader = new QuestionsLoader();
        GameFactory gameFactory = new GameFactory(eventPublisher, questionsLoader);
        final String[] strings = new String[]{"Chet", "Pat", "Sue", "Joe", "Vlad"};
        final String string = strings[0];
        final String[] strings1 = new String[]{string, "Chet", "Pat", "Sue", "Joe", "Vlad"};
        final String s = strings1[0];
        Game game = gameFactory.create("game name", s, string, "Chet", "Pat", "Sue", "Joe", "Vlad");
        game.play();
    }
}
