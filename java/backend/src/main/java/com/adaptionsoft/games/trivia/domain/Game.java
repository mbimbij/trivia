package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.*;
import com.adaptionsoft.games.trivia.domain.exception.*;
import com.adaptionsoft.games.trivia.domain.statemachine.CannotExecuteAction;
import com.adaptionsoft.games.trivia.domain.statemachine.StateManager;
import com.adaptionsoft.games.trivia.domain.statemachine.Transition;
import com.adaptionsoft.games.trivia.microarchitecture.Entity;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import static com.adaptionsoft.games.trivia.domain.GameAction.END_GAME;
import static com.adaptionsoft.games.trivia.domain.GameAction.*;
import static com.adaptionsoft.games.trivia.domain.GameState.*;
import static com.adaptionsoft.games.trivia.domain.PlayerAction.*;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Setter // for testing purposes only
public class Game extends Entity<GameId> {
    @Getter
    private final String name;
    private final Players players;
    private final StateManager stateManager;
    private Dice dice;
    @Getter
    private boolean isGameInProgress = true;
    @Getter
    private int turn = 0;
    @Getter
    private Player currentPlayer;
    @Getter
    private Player winner;
    private final Board board;
    @Getter
    private GameState state;
    private QuestionsDeck questionsDeck;
    @Getter
    private Question currentQuestion;
    @Getter
    private Dice.Roll currentRoll;

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.players.setCurrent(currentPlayer);
    }

    public Game(GameId gameId,
                String name,
                GameState state,
                EventPublisher eventPublisher,
                Board board,
                Dice dice,
                QuestionsDeck questionsDeck,
                Player creator,
                StateManager stateManager,
                Player... otherPlayers) {
        super(gameId, eventPublisher);
        this.name = name;
        this.players = new Players(eventPublisher, creator, otherPlayers);
        this.dice = dice;
        this.currentPlayer = this.players.getCurrent();
        this.board = board;
        this.state = state;
        this.questionsDeck = questionsDeck;
        this.stateManager = new StateManager("game - name:%s - id:%s".formatted(name, gameId),
                CREATED,
                new Transition(CREATED, JOIN, CREATED),
                new Transition(CREATED, START, STARTED),
                new Transition(STARTED, GameAction.END_TURN, STARTED),
                new Transition(STARTED, END_GAME, ENDED)
        );
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

    public void addPlayer(Player player) {
        stateManager.validateAction(JOIN);
        player.setGameId(id);
        players.add(player);
        stateManager.applyAction(JOIN);
    }

    public void start(Player player) {
        stateManager.validateAction(START);
        if (!isCreator(player)) {
            throw StartException.onlyCreatorCanStartGame(id, player.getId());
        }
        if (playersCountLessThanMinimumRequired()) {
            throw StartException.invalidNumberOfPlayers(id, players.count());
        }

        state = STARTED;
        turn = 1;
        raise(new GameStartedEvent(id),
                new PlayerTurnStartedEvent(currentPlayer, currentPlayer.getTurn()));

        stateManager.applyAction(START);

        eventPublisher.flushEvents();
    }

    private boolean isCreator(Player player) {
        return Objects.equals(player, players.getCreator());
    }

    private boolean playersCountLessThanMinimumRequired() {
        return players.count() < Players.MIN_PLAYER_COUNT_AT_START_TIME;
    }

    public void rollDice(Player player) {
        validateGameStartedForPlayerAction(ROLL_DICE);
        validateCurrentPlayer(player);
        currentPlayer.validateAction(ROLL_DICE);

        currentRoll = dice.roll();
        currentPlayer.applyAction(ROLL_DICE);
        raise(new PlayerRolledDiceEvent(player, currentRoll, currentPlayer.getTurn()));
        if (player.isInPenaltyBox()) {
            rollDiceFromPenaltyBox(player);
        } else {
            board.movePlayer(player, currentRoll);
            currentPlayer.applyAction(UPDATE_LOCATION);
        }

        eventPublisher.flushEvents();
    }

    private void validateGameStartedForPlayerAction(PlayerAction playerAction) {
        try {
            stateManager.validateState(STARTED);
        } catch (AssertionError e) {
            throw new CannotExecuteAction(stateManager.getEntityIdentifier(), playerAction, stateManager.getCurrentState());
        }
    }

    private void rollDiceFromPenaltyBox(Player player) {
        if (currentRoll.isPair()) {
            player.getOutOfPenaltyBox();
            board.movePlayer(player, currentRoll);
            currentPlayer.applyAction(UPDATE_LOCATION);
        } else {
            currentPlayer.applyAction(STAY_IN_PENALTY_BOX);
            raise(new PlayerStayedInPenaltyBoxEvent(player, player.getTurn()));
            endTurn();
        }
    }

    public void drawQuestion(Player currentPlayer) {
        validateGameStartedForPlayerAction(DRAW_QUESTION);
        validateCurrentPlayer(currentPlayer);
        currentPlayer.validateAction(DRAW_QUESTION);

        this.currentQuestion = questionsDeck.drawQuestion(currentPlayer.getLocation());
        this.currentPlayer.applyAction(DRAW_QUESTION);
        raise(new QuestionAskedToPlayerEvent(currentPlayer, currentQuestion.questionText()));
        eventPublisher.flushEvents();
    }

    public boolean answerCurrentQuestion(Player player, AnswerCode answerCode) {
        validateGameStartedForPlayerAction(SUBMIT_ANSWER);
        validateCurrentPlayer(player);
        currentPlayer.applyAction(SUBMIT_ANSWER);

        boolean isAnswerCorrect = false;

        if (currentQuestion == null) {
            throw new CannotAnswerQuestionBeforeDrawingOneException(id, player.getId());
        }

        if (currentQuestion.isCorrect(answerCode)) {
            isAnswerCorrect = true;
            currentPlayer.answerCorrectly();
            endGameIfCurrentPlayerWon();
            if (isGameInProgress) {
                endTurn();
            }
        } else {
            currentPlayer.answerIncorrectly();
            if (currentPlayer.canContinueAfterIncorrectAnswer()) {
                drawQuestion(currentPlayer);
            } else {
                endTurn();
            }
        }
        eventPublisher.flushEvents();
        return isAnswerCorrect;
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
            stateManager.applyAction(END_GAME);
            currentPlayer.applyAction(PlayerAction.END_GAME);
        }
    }

    private void endTurn() {
        currentPlayer.applyAction(PlayerAction.END_TURN);
        stateManager.applyAction(GameAction.END_TURN);
        currentQuestion = null;
        currentRoll = null;
        turn++;
        players.goToNextPlayerTurn();
        currentPlayer = players.getCurrent();
        displayCurrentPlayerIfGameNotEnded();
    }

    public void setState(GameState gameState) {
        this.state=gameState;
        stateManager.setCurrentState(gameState);
    }
}
