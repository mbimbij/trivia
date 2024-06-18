package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.*;
import com.adaptionsoft.games.trivia.domain.exception.InvalidGameStateException;
import com.adaptionsoft.games.trivia.domain.exception.PlayTurnException;
import com.adaptionsoft.games.trivia.domain.exception.StartException;
import com.adaptionsoft.games.trivia.microarchitecture.Entity;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import lombok.*;

import java.util.*;

import static com.adaptionsoft.games.trivia.domain.Game.State.*;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Game extends Entity<GameId> {
    @Getter
    private final String name;
    private final Players players;
    private boolean isGameInProgress = true;
    @Getter
    private int turn = 0;
    private Player currentPlayer;
    @Getter
    private Player winner;
    private final Random rand;
    private final Board board;
    private final PlayerTurnOrchestrator playerTurnOrchestrator;
    @Getter
    @Setter // for testing purposes only
    private State state;
    private final Questions questions;
    @Getter
    private Question currentQuestion;

    /**
     * All args constructor only used for custom withers used in tests
     */
    private Game(GameId id,
                EventPublisher eventPublisher,
                String name,
                Players players,
                boolean isGameInProgress,
                int turn,
                Player currentPlayer,
                Player winner,
                Random rand,
                Board board,
                PlayerTurnOrchestrator playerTurnOrchestrator,
                State state,
                Questions questions,
                Question currentQuestion) {
        super(id, eventPublisher);
        this.name = name;
        this.players = players;
        this.isGameInProgress = isGameInProgress;
        this.turn = turn;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
        this.rand = rand;
        this.board = board;
        this.playerTurnOrchestrator = playerTurnOrchestrator;
        this.state = state;
        this.questions = questions;
        this.currentQuestion = currentQuestion;
    }

    public Game withDice(Random rand) {
        return new Game(id, eventPublisher,
                name,
                players,
                isGameInProgress,
                turn,
                currentPlayer,
                winner,
                rand,
                board,
                playerTurnOrchestrator,
                state,
                questions,
                currentQuestion);
    }

    public Game(GameId gameId,
                String name,
                EventPublisher eventPublisher,
                Players players,
                Random rand,
                PlayerTurnOrchestrator playerTurnOrchestrator,
                Player currentPlayer,
                Board board,
                State state,
                Questions questions) {
        super(gameId, eventPublisher);
        this.name = name;
        this.players = players;
        this.rand = rand;
        this.playerTurnOrchestrator = playerTurnOrchestrator;
        this.currentPlayer = currentPlayer;
        this.board = board;
        this.state = state;
        this.questions = questions;
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
        endTurnAndGoToNextPlayer();
        eventPublisher.flushEvents();
    }

    public void submitAnswerToCurrentQuestion(Player player, AnswerCode answerCode) {
        validateGameStateIs(STARTED, "answer question");
        if (!Objects.equals(player, currentPlayer)) {
            throw PlayTurnException.notCurrentPlayerException(id, currentPlayer.getId(), currentPlayer.getId());
        }
        if (currentQuestion.isCorrect(answerCode)) {
            currentPlayer.answerCorrectly();
            endTurnAndGoToNextPlayer();
            currentPlayer.updateLocation(computeNewPlayerLocation(rollDice()));
            currentQuestion = questions.drawQuestion(currentPlayer.getLocation());
            raise(new QuestionAskedToPlayerEvent(currentPlayer, currentQuestion.questionText(), currentPlayer.getTurn()));
        } else {
            currentPlayer.answerIncorrectly();
            if (currentPlayer.canContinueAfterIncorrectAnswer()) {
                currentQuestion = questions.drawQuestion(currentPlayer.getLocation());
                raise(new QuestionAskedToPlayerEvent(currentPlayer, currentQuestion.questionText(), currentPlayer.getTurn()));
            } else {
                endTurnAndGoToNextPlayer();
                currentPlayer.updateLocation(computeNewPlayerLocation(rollDice()));
                currentQuestion = questions.drawQuestion(currentPlayer.getLocation());
                raise(new QuestionAskedToPlayerEvent(currentPlayer, currentQuestion.questionText(), currentPlayer.getTurn()));
            }
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

    private void displayCurrentPlayerIfGameNotEnded() {
        if (state != ENDED) {
            raise(new PlayerTurnStartedEvent(currentPlayer, currentPlayer.getTurn()));
        }
    }

    private void endGameIfCurrentPlayerWon() {
        if (currentPlayer.isWinning()) {
            isGameInProgress = false;
            state = ENDED;
            winner = currentPlayer;
            raise(new PlayerWonEvent(id, currentPlayer, currentPlayer.getTurn()));
            raise(new GameEndedEvent(id, currentPlayer.getId()));
        }
    }

    private void endTurnAndGoToNextPlayer() {
        players.goToNextPlayerTurn();
        currentPlayer = players.getCurrent();
        turn++;
        if (state != ENDED) {
            raise(new PlayerTurnStartedEvent(currentPlayer, currentPlayer.getTurn()));
        }
    }

    public void addPlayer(Player player) {
        if (!state.equals(CREATED)) {
            throw new InvalidGameStateException(this.getId(), this.getState(), "add player");
        }
        player.setGameId(id);
        players.add(player);
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
        raise(new GameStartedEvent(id));
        raise(new PlayerTurnStartedEvent(currentPlayer, currentPlayer.getTurn()));
        eventPublisher.flushEvents();
    }

    int computeNewPlayerLocation(int roll) {
        return (currentPlayer.getLocation() + roll) % board.getSquaresCount();
    }

    // TODO extract delegate ?
    public void rollDiceBy(Player player) {
        int roll = rollDice();
        if (player.isInPenaltyBox()) {
            raise(new PlayerStayedInPenaltyBoxEvent(currentPlayer, currentPlayer.getTurn()));
            endTurnAndGoToNextPlayer();
        } else {
            currentPlayer.updateLocation(computeNewPlayerLocation(roll));
            currentQuestion = questions.drawQuestion(currentPlayer.getLocation());
            raise(new QuestionAskedToPlayerEvent(currentPlayer, currentQuestion.questionText(), turn));
        }
        eventPublisher.flushEvents();
    }

    int rollDice() {
        int roll = rand.nextInt(5) + 1;
        raise(new PlayerRolledDiceEvent(currentPlayer, roll, currentPlayer.getTurn()));
        return roll;
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
