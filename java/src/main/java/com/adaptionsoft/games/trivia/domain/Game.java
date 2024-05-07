package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.Event;
import com.adaptionsoft.games.trivia.domain.event.GameEndedEvent;
import com.adaptionsoft.games.trivia.domain.event.GameStartedEvent;
import com.adaptionsoft.games.trivia.domain.event.PlayerWonEvent;
import com.adaptionsoft.games.trivia.domain.exception.InvalidGameStateException;
import com.adaptionsoft.games.trivia.domain.exception.PlayTurnException;
import com.adaptionsoft.games.trivia.domain.exception.StartException;
import com.adaptionsoft.games.trivia.microarchitecture.Entity;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

import static com.adaptionsoft.games.trivia.domain.Game.State.*;

@EqualsAndHashCode(callSuper = true)
public class Game extends Entity {

    @Getter
    private final String name;
    private final EventPublisher eventPublisher;
    private final Players players;
    private boolean isGameInProgress = true;
    @Getter
    int turn = 0;
    private Player currentPlayer;
    private PlayerTurnOrchestrator playerTurnOrchestrator;
    @Getter
    @Setter // for testing purposes only
    private State state;


    // do not call directly, unless in a testing context
    public Game(String name,
                EventPublisher eventPublisher,
                Players players,
                PlayerTurnOrchestrator playerTurnOrchestrator,
                Player currentPlayer,
                State state) {
        this.name = name;
        this.eventPublisher = eventPublisher;
        this.players = players;
        this.playerTurnOrchestrator = playerTurnOrchestrator;
        this.currentPlayer = currentPlayer;
        this.state = state;
    }

    public Game(Integer id,
                String name,
                EventPublisher eventPublisher,
                Players players,
                PlayerTurnOrchestrator playerTurnOrchestrator,
                Player currentPlayer,
                State state) {
        this(name, eventPublisher, players, playerTurnOrchestrator, currentPlayer, state);
        this.id = id;
    }

    public Player getCreator() {
        return players.getCreator();
    }

    public void play() {
        do {
            playTurnBy(currentPlayer);
        } while (isGameInProgress);
    }

    public void playTurnBy(Player player) {
        validateGameNotEnded("play turn");
        if (!Objects.equals(player, currentPlayer)) {
            throw PlayTurnException.notCurrentPlayerException(id, player.getId(), currentPlayer.getId());
        }
        playerTurnOrchestrator.performTurn(player);
        endGameIfCurrentPlayerWon();
        publishDomainEvents();
        endCurrentPlayerTurn();
    }

    private void validateGameNotEnded(String action) {
        if (state.equals(ENDED)) {
            throw new InvalidGameStateException(this.getId(), this.getState(), action);
        }
    }

    private void endGameIfCurrentPlayerWon() {
        if (currentPlayer.isWinning()) {
            isGameInProgress = false;
            state = ENDED;
            raise(new PlayerWonEvent(id, currentPlayer));
            raise(new GameEndedEvent(id, currentPlayer.getId()));
        }
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
            throw new InvalidGameStateException(this.getId(), this.getState(), "add player");
        }
        players.addAfterCreationTime(player);
    }

    public void startBy(Player player) {
        validateGameNotEnded("start");
        if (!Objects.equals(player, players.getCreator())) {
            throw StartException.onlyCreatorCanStartGame(id, player.getId());
        }
        if (players.count() < Players.MIN_PLAYER_COUNT_AT_START_TIME) {
            throw StartException.invalidNumberOfPlayers(id, players.count());
        }
        state = STARTED;
        turn = 1;
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

    public Player getCurrentPlayer() {
        return players.getCurrent();
    }

    public Collection<Player> getPlayersList() {
        return players.getIndividualPlayers();
    }

    public int getPlayersCount() {
        return players.count();
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
