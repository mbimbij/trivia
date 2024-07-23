import {PlayerDto, UserDto} from "../openapi-generated";
import {User} from "../user/user";
import {Player} from "../user/player";

// TODO move these functions into appropriate classes
export function compareUserDtos(user1: UserDto, user2: UserDto): boolean {
  return user1 !== null && user2 !== null && user1.constructor === user2.constructor && user1.id === user2.id;
}

export function compareUserAndPlayer(user: User, player: PlayerDto | Player): boolean {
  return user !== null && player !== null && user.id === player.id;
}

export function comparePlayers(player1?: Player, player2?: Player): boolean {
  return player1 !== null && player2 !== null && player1?.id === player2?.id;
}

export function userToPlayerDto(user: User): PlayerDto {
  return {id: user.id, name: user.name, coinCount: 0, isInPenaltyBox: false, consecutiveIncorrectAnswersCount:0, state: ""}
}

export function userToPlayer(user: User): Player {
  return new Player(user.id, user.name, 0, false, 0, "")
}

export function userToUserDto(user: User): UserDto {
  return {id: user.id, name: user.name}
}

export function playerDtoToPlayer(dto: PlayerDto): Player {
  return new Player(dto.id,
    dto.name,
    dto.coinCount,
    dto.isInPenaltyBox,
    dto.consecutiveIncorrectAnswersCount,
    dto.state
  )
}

export function playerToPlayerDto(player: Player): PlayerDto {
  return {
    id: player.id,
    name: player.name,
    coinCount: player.coinCount,
    isInPenaltyBox: player.isInPenaltyBox,
    consecutiveIncorrectAnswersCount: player.consecutiveIncorrectAnswersCount,
    state: player.state
  }
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
