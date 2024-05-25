import {PlayerDto, UserDto} from "../openapi-generated";
import {User} from "../user/user";
import {Player} from "../user/player";

// TODO move these functions into appropriate classes
export function comparePlayerDto(user1: UserDto, user2: UserDto): boolean {
  return user1 !== null && user2 !== null && user1.constructor === user2.constructor && user1.id === user2.id;
}

export function compareUserAndPlayer(user: User, player: PlayerDto): boolean {
  return user !== null && player !== null && user.id === player.id;
}

export function comparePlayers(player1?: PlayerDto, player2?: PlayerDto): boolean {
  return player1 !== null && player2 !== null && player1?.id === player2?.id;
}

export function userToPlayerDto(user: User): PlayerDto {
  return {id: user.id, name: user.name, coinCount: 0}
}

export function userToUserDto(user: User): UserDto {
  return {id: user.id, name: user.name}
}

export function playerDtoToPlayer(dto: PlayerDto): Player {
  return {id: dto.id, name: dto.name, coinCount: dto.coinCount}
}

export function playerToPlayerDto(dto: PlayerDto): PlayerDto {
  return new Player(dto.id, dto.name, dto.coinCount)
}

export function getRandomInt(max: number) {
  return Math.floor(Math.random() * max);
}

export function randomPlayer(): Player {
  return new Player(`id-${generateRandomString(4)}`, `name-${generateRandomString(4)}`, 0)
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
