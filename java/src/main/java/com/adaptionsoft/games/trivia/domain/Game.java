package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.Event;
import com.adaptionsoft.games.trivia.microarchitecture.Entity;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static com.adaptionsoft.games.trivia.domain.Game.State.CREATED;
import static com.adaptionsoft.games.trivia.domain.Game.State.STARTED;

@EqualsAndHashCode(callSuper = true)
public class Game extends Entity {

    @Getter
    private final String name;
    private final EventPublisher eventPublisher;

    @Getter
    private final Players players;

    private final Questions questions;
    private final Random rand;
    private final Board board;

    private boolean isGameInProgress = true;
    int turn = 1;
    private Player currentPlayer;
    private PlayerTurnOrchestrator playerTurnOrchestrator;
    @Getter
    private State state;


    // do not call directly, unless in a testing context
    public Game(String name, EventPublisher eventPublisher, Players players, Questions questions, Random rand, Board board) {
        this.name = name;
        this.eventPublisher = eventPublisher;
        this.players = players;
        this.questions = questions;
        this.rand = rand;
        this.board = board;
        currentPlayer = players.getCurrent();
        playerTurnOrchestrator = new PlayerTurnOrchestrator(questions, rand, board);
        state = CREATED;
    }

    public void play() {
        do {
            performGameTurn();
        } while (isGameInProgress);
    }

    private void performGameTurn() {
        playerTurnOrchestrator.performTurn(currentPlayer);
        endGameIfCurrentPlayerWon();
        publishDomainEvents();
        endCurrentPlayerTurn();
    }

    private void endGameIfCurrentPlayerWon() {
        this.isGameInProgress = !currentPlayer.isWinning();
    }

    private void endCurrentPlayerTurn() {
        players.goToNextPlayerTurn();
        currentPlayer = players.getCurrent();
        turn++;
    }

    private void publishDomainEvents() {
        List<Event> aggregatedEvents = getAndClearUncommittedEvents();
        aggregatedEvents.addAll(currentPlayer.getAndClearUncommittedEvents());
        aggregatedEvents.addAll(playerTurnOrchestrator.getAndClearUncommittedEvents());
        aggregatedEvents.sort(Comparator.comparingInt(Event::getOrderNumber));
        eventPublisher.publish(aggregatedEvents);
    }

    public enum State {
        CREATED("created"),
        STARTED("started"),
        ENDED("ended"),
        ;

        private final String value;

        State(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
