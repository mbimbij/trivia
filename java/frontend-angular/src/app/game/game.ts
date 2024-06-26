import {Player} from "../user/player";
import {GameResponseDto, QuestionDto} from "../openapi-generated";
import {comparePlayers, playerDtoToPlayer, playerToPlayerDto} from "../common/helpers";

export class Game {
  id: number
  name: string
  state: string;
  turn: number;
  creator: Player;
  currentPlayer: Player;
  players: Player[];
  winner: Player | undefined
  currentQuestion: QuestionDto | undefined
  currentRoll: number | undefined

  constructor(id: number,
              name: string,
              state: string,
              turn: number,
              creator: Player,
              currentPlayer: Player,
              players: Player[],
              winner?: Player,
              currentQuestion?: QuestionDto,
              currentRoll?: number) {
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
  }

  static fromDto(dto: GameResponseDto): Game {
    return new Game(dto.id,
      dto.name,
      dto.state,
      dto.turn,
      playerDtoToPlayer(dto.creator),
      playerDtoToPlayer(dto.currentPlayer),
      dto.players.map(
        playerDto => playerDtoToPlayer(playerDto)
      ),
      dto.winner ? playerDtoToPlayer(dto.winner) : undefined,
      dto.currentQuestion,
      dto.currentRoll
    )
  }

  toDto(): GameResponseDto {
    return {
      id: this.id,
      name: this.name,
      state: this.state,
      turn: this.turn,
      creator: playerToPlayerDto(this.creator),
      currentPlayer: playerToPlayerDto(this.currentPlayer),
      players: this.players.map(player => playerToPlayerDto(player)),
      currentQuestion: this.currentQuestion,
      currentRoll: this.currentRoll,
    }
  }

  public isCurrentPlayer(player: Player): boolean {
    return comparePlayers(player, this.currentPlayer)
  }

  public canRollDice(player: Player): boolean {
    return this.isCurrentPlayer(player) && this.currentRoll == undefined
  }

  public canDrawQuestion(player: Player): boolean {
    return this.isCurrentPlayer(player) && this.currentRoll != undefined && this.currentQuestion == undefined
  }

  public canAnswerQuestion(player: Player): boolean {
    return this.isCurrentPlayer(player) && this.currentRoll != undefined && this.currentQuestion != undefined
  }

  public getCurrentStateOf(player: Player): Player {
    return this.players.find(p => p.id === player.id)!
  }
}
