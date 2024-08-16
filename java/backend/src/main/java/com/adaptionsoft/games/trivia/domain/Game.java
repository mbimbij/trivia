package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.*;
import com.adaptionsoft.games.trivia.domain.exception.CannotAnswerQuestionBeforeDrawingOneException;
import com.adaptionsoft.games.trivia.domain.exception.PlayTurnException;
import com.adaptionsoft.games.trivia.domain.exception.StartException;
import com.adaptionsoft.games.trivia.domain.statemachine.CannotExecuteAction;
import com.adaptionsoft.games.trivia.domain.statemachine.State;
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
    @Getter
    private Answer currentAnswer;
    @Getter
    private QuestionsDeck.Category currentCategory;

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.players.setCurrent(currentPlayer);
    }

    public State getCurrentPlayerState() {
        return currentPlayer.getState();
    }

    public Game(GameId gameId,
                String name,
                GameState state,
                EventPublisher eventPublisher,
                Board board,
                Dice dice,
                QuestionsDeck questionsDeck,
                Player creator,
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
        questionsDeck.shuffle();
        shufflePlayers();
        raise(new GameStartedEvent(id),
                new PlayerTurnStartedEvent(currentPlayer, currentPlayer.getTurn()));

        stateManager.applyAction(START);

        eventPublisher.flush();
    }

    private void shufflePlayers() {
        players.shuffle();
        this.currentPlayer = players.getCurrent();
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
        player.validateAction(ROLL_DICE);

        currentRoll = dice.roll();
        int newLocationIfOutOfPenaltyBox = board.computeNewLocation(player, currentRoll);

        currentPlayer.applyDiceRoll(currentRoll, newLocationIfOutOfPenaltyBox);

        currentCategory = QuestionsDeck.Category.getQuestionCategory(currentPlayer.getLocation());

        eventPublisher.flush();
    }

    private void validateGameStartedForPlayerAction(PlayerAction playerAction) {
        try {
            stateManager.validateState(STARTED);
        } catch (AssertionError e) {
            throw new CannotExecuteAction(stateManager.getEntityIdentifier(), playerAction, stateManager.getCurrentState());
        }
    }

    public void drawQuestion(Player player) {
        validateGameStartedForPlayerAction(DRAW_QUESTION);
        validateCurrentPlayer(player);
        player.validateAction(DRAW_QUESTION);

        this.currentQuestion = questionsDeck.drawQuestion(player.getLocation());
        player.applyAction(DRAW_QUESTION);
        raise(new QuestionAskedToPlayerEvent(player, currentQuestion.questionText()));
        eventPublisher.flush();
    }

    public Answer answerCurrentQuestion(Player player, AnswerCode answerCode) {
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
        } else {
            currentPlayer.answerIncorrectly();
        }
        eventPublisher.flush();
        currentAnswer = new Answer(isAnswerCorrect, currentQuestion.explanations());
        return currentAnswer;
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
            currentAnswer = null;
            currentRoll = null;
            currentCategory = null;
            raise(new PlayerWonEvent(id, winner, winner.getTurn()));
            raise(new GameEndedEvent(id, winner.getName()));
            stateManager.applyAction(END_GAME);
            winner.applyAction(PlayerAction.END_GAME);
        }
    }

    private void endTurn() {
        currentPlayer.setGotOutOfPenaltyBox(false);
        currentPlayer.applyAction(PlayerAction.END_TURN);
        stateManager.applyAction(GameAction.END_TURN);
        currentQuestion = null;
        currentRoll = null;
        currentCategory = null;
        currentAnswer = null;
        turn++;
        players.goToNextPlayerTurn();
        currentPlayer = players.getCurrent();
        displayCurrentPlayerIfGameNotEnded();
    }

    public void setState(GameState gameState) {
        this.state = gameState;
        stateManager.setCurrentState(gameState);
    }

    public void validate(Player player) {
        validateGameStartedForPlayerAction(VALIDATE);
        validateCurrentPlayer(player);
        currentPlayer.validateAction(VALIDATE);
        currentPlayer.applyAction(VALIDATE);
        currentAnswer = null;

        switch (currentPlayer.getState()) {
            case PlayerState.WAITING_TO_END_TURN_OR_GAME -> endTurnOrGame();
            case PlayerState.WAITING_TO_DRAW_2ND_QUESTION -> drawQuestion(currentPlayer);
            case PlayerState.IN_PENALTY_BOX -> endTurn();
            case PlayerState.WAITING_TO_UPDATE_LOCATION -> updateCurrentPlayerLocation();
            default ->
                    throw new IllegalStateException("invalidate state for VALIDATE action: %s".formatted(player.getState()));
        }

        eventPublisher.flush();
    }

    private void updateCurrentPlayerLocation() {
        currentPlayer.updateLocation(board.computeNewLocation(currentPlayer, currentRoll));
        currentPlayer.applyAction(UPDATE_LOCATION);
    }

    private void endTurnOrGame() {
        endGameIfCurrentPlayerWon();
        if (isGameInProgress) {
            endTurn();
        }
    }

    public void setQuestionsShuffler(QuestionsShuffler shuffler) {
        questionsDeck.setShuffler(shuffler);
    }

    public void setPlayersShuffler(PlayersShuffler shuffler) {
        players.setShuffler(shuffler);
    }

}
