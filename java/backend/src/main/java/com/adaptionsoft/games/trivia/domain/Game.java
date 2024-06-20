package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.*;
import com.adaptionsoft.games.trivia.domain.exception.*;
import com.adaptionsoft.games.trivia.microarchitecture.Entity;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import lombok.*;

import java.util.*;

import static com.adaptionsoft.games.trivia.domain.State.*;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Game extends Entity<GameId> {
    @Getter
    private final String name;
    private final Players players;
    @Getter
    private boolean isGameInProgress = true;
    @Getter
    private int turn = 0;
    @Setter
    private Player currentPlayer;
    @Getter
    private Player winner;
    @Setter
    private Random rand;
    private final Board board;
    @Getter
    @Setter // for testing purposes only
    private State state;
    private final Questions questions;
    @Getter
    @Setter
    private Question currentQuestion;
    @Getter
    private Integer currentRoll;

    public Game(GameId gameId,
                String name,
                EventPublisher eventPublisher,
                Players players,
                Random rand,
                Player currentPlayer,
                Board board,
                State state,
                Questions questions) {
        super(gameId, eventPublisher);
        this.name = name;
        this.players = players;
        this.rand = rand;
        this.currentPlayer = currentPlayer;
        this.board = board;
        this.state = state;
        this.questions = questions;
    }

    public Optional<Player> findPlayerById(UserId playerId) {
        return players.getIndividualPlayers()
                .stream()
                .filter(player -> Objects.equals(player.getId(), playerId))
                .findAny();
    }

    public Player getCreator() {
        return players.getCreator();
    }

    public void submitAnswerToCurrentQuestion(Player player, AnswerCode answerCode) {
        validateGameStateIs(STARTED, "answer question");
        validateCurrentPlayer(player);
        validatePlayerNotInPenaltyBox(player, "submit answer");
        if(currentQuestion == null) {
            throw new CannotAnswerQuestionBeforeDrawingOneException(id, player.getId());
        }

        if (currentQuestion.isCorrect(answerCode)) {
            currentPlayer.answerCorrectly();
            endGameIfCurrentPlayerWon();
            if (isGameInProgress) {
                endTurn();
            }
        } else {
            currentPlayer.answerIncorrectly();
            if (currentPlayer.canContinueAfterIncorrectAnswer()) {
                currentQuestion = drawQuestion(currentPlayer);
            } else {
                endTurn();
            }
        }
        eventPublisher.flushEvents();
    }

    public Question drawQuestion(Player currentPlayer) {
        validateGameStateIs(STARTED, "draw question");
        validateCurrentPlayer(currentPlayer);
        validatePlayerNotInPenaltyBox(currentPlayer, "draw question");
        if(currentRoll == null){
            throw new CannotDrawQuestionBeforeRollingDiceException(getId(), currentPlayer.getId());
        }

        Question currentQuestion = questions.drawQuestion(currentPlayer.getLocation());
        raise(new QuestionAskedToPlayerEvent(currentPlayer, currentQuestion.questionText()));
        return currentQuestion;
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

    public void rollDiceAndDrawQuestion(Player player) {
        validateGameStateIs(STARTED, "answer question");
        validateCurrentPlayer(player);
        if (!player.canRollDice()) {
            throw new RollDiceException(id, player.getId());
        }

        currentRoll = rollDice();
        if (player.isInPenaltyBox()) {
            if (isPair(currentRoll)) {
                player.getOutOfPenaltyBox();
                raise(new PlayerGotOutOfPenaltyBoxEvent(player, player.getTurn()));
                player.updateLocation(computeNewPlayerLocation(currentRoll));
                currentQuestion = drawQuestion(player);
            } else {
                raise(new PlayerStayedInPenaltyBoxEvent(player, player.getTurn()));
                endTurn();
            }
        } else {
            player.updateLocation(computeNewPlayerLocation(currentRoll));
            currentQuestion = drawQuestion(player);
        }

        eventPublisher.flushEvents();
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

    private void validateCurrentPlayer(Player player) {
        if (!Objects.equals(player, currentPlayer)) {
            throw PlayTurnException.notCurrentPlayerException(id, player.getId(), currentPlayer.getId());
        }
    }

    private void validatePlayerNotInPenaltyBox(Player player, String actionName) {
        if(player.isInPenaltyBox()){
            throw new ExecuteActionInPenaltyBoxException(getId(), player, actionName);
        }
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
            raise(new GameEndedEvent(id, currentPlayer.getName()));
        }
    }

    private void endTurn() {
        currentQuestion = null;
        currentRoll = null;
        turn++;
        players.goToNextPlayerTurn();
        currentPlayer = players.getCurrent();
        displayCurrentPlayerIfGameNotEnded();
    }

    int computeNewPlayerLocation(int roll) {
        return (currentPlayer.getLocation() + roll) % board.getSquaresCount();
    }
    // TODO extract delegate ?

    boolean isPair(int roll) {
        return roll % 2 == 0;
    }
    // TODO extract delegate ?

    int rollDice() {
        int roll = rand.nextInt(5) + 1;
        raise(new PlayerRolledDiceEvent(currentPlayer, roll, currentPlayer.getTurn()));
        return roll;
    }
}
