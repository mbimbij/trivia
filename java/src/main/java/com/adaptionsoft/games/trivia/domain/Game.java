package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.Event;
import com.adaptionsoft.games.trivia.microarchitecture.Entity;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static com.adaptionsoft.games.trivia.domain.Game.State.CREATED;

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
    @Setter // for testing purposes only
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

    public void addPlayer(Player player) {
        if(!state.equals(CREATED)){
            throw new AddPlayerInvalidStateException(this);
        }
        players.addAfterCreationTime(player);
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

    public static class AddPlayerInvalidStateException extends RuntimeException{
        public AddPlayerInvalidStateException(Game game) {
            super("Tried to add player for game=%d with state='%s'".formatted(game.getId(), game.getState()));
        }
    }
}
