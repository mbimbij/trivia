package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.Event;
import com.adaptionsoft.games.trivia.domain.event.GameStartedEvent;
import com.adaptionsoft.games.trivia.microarchitecture.BusinessException;
import com.adaptionsoft.games.trivia.microarchitecture.Entity;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

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
        if (!state.equals(CREATED)) {
            throw new AddPlayerInvalidStateException(this.getId(), this.getState());
        }
        players.addAfterCreationTime(player);
    }

    public void startBy(Player player) {
        if (!Objects.equals(player, players.getCreator())) {
            throw StartException.onlyCreatorCanStartGame(id, player.getId());
        }
        if (players.size() < Players.MIN_PLAYER_COUNT_AT_START_TIME) {
            throw StartException.invalidNumberOfPlayers(id, players.size());
        }
        state = STARTED;
        // TODO régler publication des events uncommitted
        raise(new GameStartedEvent(id));
        publishDomainEvents();
    }

    public Optional<Player> findPlayerById(Integer playerId) {
        return players.getIndividualPlayers()
                .stream()
                .filter(player -> Objects.equals(player.getId(), playerId))
                .findAny();
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

    public static abstract class GameException extends BusinessException {

        protected final Integer gameId;

        protected GameException(Integer gameId, String message) {
            super(message);
            this.gameId = gameId;
        }
    }

    public static class AddPlayerInvalidStateException extends GameException {
        public AddPlayerInvalidStateException(Integer gameId, State gameState) {
            super(gameId, "Tried to add player for game=%d with state='%s'".formatted(gameId, gameState));
        }
    }

    public static class StartException extends GameException {
        private StartException(Integer gameId, String message) {
            super(gameId, message);
        }

        public static StartException onlyCreatorCanStartGame(Integer gameId, Integer playerId) {
            return new StartException(gameId, "player id=%d tried to start game id=%d but is not the creator".formatted(playerId, gameId));
        }

        public static StartException invalidNumberOfPlayers(Integer gameId, int numberOfPlayers) {
            return new StartException(gameId, "game id=%d must have between %d and %d players to start, but was %d".formatted(gameId,
                    Players.MIN_PLAYER_COUNT_AT_START_TIME,
                    Players.MAX_PLAYER_COUNT,
                    numberOfPlayers));
        }
    }
}
