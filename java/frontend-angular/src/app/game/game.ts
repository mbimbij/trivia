import {Player} from "../user/player";
import {comparePlayers, compareUserAndPlayer} from "../shared/helpers";
import {AnswerDto, QuestionDto, State} from "../openapi-generated/game";
import {User} from "../user/user";

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

  public canJoin(user: User): boolean {
    return !this.isPlayer(user) && isPlayersCountValid(this.players.length) && !this.isStarted()

    function isPlayersCountValid(playersCount: number) {
      return playersCount < 6;
    }
  }

  public canStart(user: User): boolean {
    return this.isPlayerCreator(user.id) && this.isGameCreated() && isPlayersCountValid(this.players.length)

    function isPlayersCountValid(playersCount: number) {
      return playersCount >= 2 && playersCount <= 6;
    }
  }

  public canGoto(user: User) {
    return this.isPlayer(user) && this.isStarted();
  }

  public canDelete(user: User) {
    return compareUserAndPlayer(user, this.creator);
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

  isPlayer(user: User) {
    return this.players.find(player => user.id === player?.id) != null;
  }

  isStarted() {
    return this.state === State.Started;
  }

  private isPlayerCreator(userId: string): boolean {
    return userId == this.creator.id;
  }

  private isGameCreated(): boolean {
    return this.state === State.Created
  }

  public getPlayersNames(): string[] {
    return this.players.map(value => value.name)
  }
}
