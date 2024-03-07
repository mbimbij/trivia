
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
        Game game = gameFactory.create( "Chet", "Pat", "Sue", "Joe", "Vlad");
        game.play();
    }
}
