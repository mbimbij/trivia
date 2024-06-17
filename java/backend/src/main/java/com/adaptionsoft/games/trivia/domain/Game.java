package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.*;
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

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Game extends Entity<GameId> {
    @Getter
    private final String name;
    private final EventPublisher eventPublisher;
    private final Players players;
    private boolean isGameInProgress = true;
    @Getter
    private int turn = 0;
    private Player currentPlayer;
    @Getter
    private Player winner;
    private final PlayerTurnOrchestrator playerTurnOrchestrator;
    @Getter
    @Setter // for testing purposes only
    private State state;
    private final Questions questions;

    public Game(GameId gameId,
                String name,
                // TODO R-1 retirer cet attribut, les joueurs devraient être rajoutés à une partie, pas à un artefact séparé
                EventPublisher eventPublisher,
                Players players,
                PlayerTurnOrchestrator playerTurnOrchestrator,
                Player currentPlayer,
                State state, Questions questions) {
        super(gameId, eventPublisher);
        this.name = name;
        this.eventPublisher = eventPublisher;
        this.players = players;
        this.playerTurnOrchestrator = playerTurnOrchestrator;
        this.currentPlayer = currentPlayer;
        this.state = state;
        this.questions = questions;
        this.players.setGameId(getId());
        this.players.raisePlayersAddedEvents();
    }

    public Player getCreator() {
        return players.getCreator();
    }

    public void play() {
        startBy(currentPlayer);
        do {
            playTurnBy(currentPlayer);
        } while (isGameInProgress);
    }

    public void playTurnBy(Player player) {
        validateGameStateIsNot(ENDED, "play turn");
        if (!Objects.equals(player, currentPlayer)) {
            throw PlayTurnException.notCurrentPlayerException(id, player.getId(), currentPlayer.getId());
        }
        playerTurnOrchestrator.performTurn(player);
        endGameIfCurrentPlayerWon();
        endCurrentPlayerTurn();
        displayNextPlayerIfGameNotEnded();
        eventPublisher.flushEvents();
    }

    public void submitAnswerToCurrentQuestion(Player player, AnswerCode answerCode) {
        validateGameStateIs(STARTED, "answer question");
        if (!Objects.equals(player, currentPlayer)) {
            throw PlayTurnException.notCurrentPlayerException(id, currentPlayer.getId(), currentPlayer.getId());
        }
        Question currentQuestion = questions.drawQuestion(currentPlayer.getLocation());
        if (currentQuestion.isCorrect(answerCode)) {
            currentPlayer.answerCorrectly();
            endCurrentPlayerTurn();
        } else {
            currentPlayer.answerIncorrectly();
        }
        eventPublisher.flushEvents();
    }

    private void validateGameStateIs(State expectedState, String action) {
        validateGameState(true, expectedState, action);
    }

    private void validateGameStateIsNot(State expectedState, String action) {
        validateGameState(false, expectedState, action);
    }

    private void validateGameState(boolean orNot, State expectedState, String action) {
        if ((!orNot && state.equals(expectedState)) || (orNot && !state.equals(expectedState))) {
            throw new InvalidGameStateException(this.getId(), this.getState(), action);
        }
    }

    private void displayNextPlayerIfGameNotEnded() {
        if (state != ENDED) {
            raise(new PlayerTurnStartedEvent(currentPlayer));
        }
    }

    private void endGameIfCurrentPlayerWon() {
        if (currentPlayer.isWinning()) {
            isGameInProgress = false;
            state = ENDED;
            winner = currentPlayer;
            raise(new PlayerWonEvent(id, currentPlayer));
            raise(new GameEndedEvent(id, currentPlayer.getId()));
        }
    }

    private void endCurrentPlayerTurn() {
        players.goToNextPlayerTurn();
        currentPlayer = players.getCurrent();
        turn++;
    }

    public void addPlayer(Player player) {
        if (!state.equals(CREATED)) {
            throw new InvalidGameStateException(this.getId(), this.getState(), "add player");
        }
        player.setGameId(id);
        players.addAfterCreationTime(player);
    }

    public void startBy(Player player) {
        validateGameStateIsNot(ENDED, "start");
        if (!Objects.equals(player, players.getCreator())) {
            throw StartException.onlyCreatorCanStartGame(id, player.getId());
        }
        if (players.count() < Players.MIN_PLAYER_COUNT_AT_START_TIME) {
            throw StartException.invalidNumberOfPlayers(id, players.count());
        }
        state = STARTED;
        turn = 1;
        // TODO repenser / clarifier la logique d'émission et publication des events, la cohérence avec des transactions, etc.
        raise(new GameStartedEvent(id));
        raise(new PlayerTurnStartedEvent(currentPlayer));
        eventPublisher.flushEvents();
    }

    public Optional<Player> findPlayerById(UserId playerId) {
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
