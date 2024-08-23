import {Player} from "../user/player";
import {comparePlayers} from "../common/helpers";
import {AnswerDto, QuestionDto, State} from "../openapi-generated/game";

export class Game {
  id: number
  name: string
  state: State;
  turn: number;
  creator: Player;
  currentPlayer: Player;
  players: Player[];
  winner: Player | undefined
  currentQuestion: QuestionDto | undefined
  currentRoll: number | undefined
  currentCategory: string | undefined
  currentAnswer: AnswerDto | undefined

  constructor(id: number,
              name: string,
              state: State,
              turn: number,
              creator: Player,
              currentPlayer: Player,
              players: Player[],
              winner?: Player,
              currentQuestion?: QuestionDto,
              currentRoll?: number,
              currentCategory?: string,
              currentAnswer?: AnswerDto
  ) {
    this.validateRollAndCategory(currentRoll, currentCategory);
    this.id = id;
    this.name = name;
    this.state = state;
    this.turn = turn;
    this.creator = creator;
    this.currentPlayer = currentPlayer;
    this.players = players;
    this.winner = winner;
    this.currentQuestion = currentQuestion;
    this.currentRoll = currentRoll;
    this.currentAnswer = currentAnswer
    this.currentCategory = currentCategory
  }

  private validateRollAndCategory(currentRoll: number | undefined, currentCategory: string | undefined) {
    if ((currentRoll != null && currentCategory == null) || (currentRoll == null && currentCategory != null)) {
      throw new Error(`currentRoll: ${currentRoll}, currentCategory: ${currentCategory}`)
    }
  }

  public isEnded(): boolean {
    return this.state === State.Ended;
  }

  public isCurrentPlayer(player: Player): boolean {
    return comparePlayers(player, this.currentPlayer)
  }

  public canRollDice(player: Player): boolean {
    return this.isCurrentPlayer(player)
      && (this.currentPlayer.state === State.WaitingForDiceRoll || this.currentPlayer.state === State.InPenaltyBox)
  }

  public canAnswerQuestion(player: Player): boolean {
    return this.isCurrentPlayer(player) && this.currentQuestion != undefined
  }

  public getCurrentStateOf(player: Player): Player {
    return this.players.find(p => p.id === player.id)!
  }

  public isWinner(player: Player): boolean {
    return comparePlayers(player, this.winner)
  }
}
