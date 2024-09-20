import {User} from "../user/user";
import {Player} from "../user/player";
import {Game} from "../game/game";
import {GameResponseDto, PlayerDto, State, UserDto} from "../openapi-generated/game";

// TODO move these functions into appropriate classes
export function compareUserDtos(user1: UserDto | null, user2: UserDto | null): boolean {
  return user1 !== null && user2 !== null && user1?.constructor === user2?.constructor && user1?.id === user2?.id;
}

export function compareUserAndPlayer(user: User | null, player: PlayerDto | Player | null): boolean {
  return user !== null && player !== null && user?.id === player?.id;
}

export function compareUserIdAndPlayer(userId: string, player: PlayerDto | Player): boolean {
  return userId === player.id;
}

export function comparePlayers(player1: Player | null, player2?: Player | null): boolean {
  return player1 !== null && player2 !== null && player1?.id === player2?.id;
}

export function userToPlayer(user: User): Player {
  return new Player(user.id,
    user.name,
    0,
    State.WaitingForDiceRoll,
    0,
    false)
}

export function userToUserDto(user: User): UserDto {
  return {id: user.id, name: user.name}
}

export function playerDtoToPlayer(dto: PlayerDto): Player {
  return new Player(dto.id,
    dto.name,
    dto.coinCount,
    dto.state,
    dto.location,
    dto.gotOutOfPenaltyBox
  )
}

export function playerToPlayerDto(player: Player): PlayerDto {
  return {
    id: player.id,
    name: player.name,
    coinCount: player.coinCount,
    state: player.state,
    location: player.location,
    gotOutOfPenaltyBox: player.gotOutOfPenaltyBox
  }
}

export function gameToGameDto(game: Game): GameResponseDto {
  return {
    id: game.id,
    name: game.name,
    state: game.state,
    turn: game.turn,
    creator: playerToPlayerDto(game.creator),
    currentPlayer: playerToPlayerDto(game.currentPlayer),
    players: game.players.map(player => playerToPlayerDto(player)),
    currentQuestion: game.currentQuestion,
    currentRoll: game.currentRoll,
    currentCategory: game.currentCategory,
    currentAnswer: game.currentAnswer
  }
}

export function gameDtoToGame(dto: GameResponseDto): Game {
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
    dto.currentRoll,
    dto.currentCategory,
    dto.currentAnswer
  )
}

export function generateRandomString(length: number): string {
  const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  let result = '';
  const charactersLength = characters.length;

  for (let i = 0; i < length; i++) {
    result += characters.charAt(Math.floor(Math.random() * charactersLength));
  }

  return result;
}
