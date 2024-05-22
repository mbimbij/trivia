import {UserDto} from "../openapi-generated";
import {User} from "../user/user";

export function compareUserDto(user1: UserDto, user2: UserDto): boolean {
  return user1 !== null && user2 !== null && user1.constructor === user2.constructor && user1.id === user2.id;
}

export function compareUserAndPlayer(user: User, player: UserDto): boolean {
  return user !== null && player !== null && user.idInteger === player.id;
}

export function comparePlayers(player1?: UserDto, player2?: UserDto): boolean {
  return player1 !== null && player2 !== null && player1?.id === player2?.id;
}

export function userToPlayer(user: User): UserDto{
  // TODO
  return {id: user.idInteger, name: user.name}
}

export function stringHashCode(str: string): number {
  var hash = 0,
    i, chr;
  if (str.length === 0) return hash;
  for (i = 0; i < str.length; i++) {
    chr = str.charCodeAt(i);
    hash = ((hash << 5) - hash) + chr;
    hash |= 0; // Convert to 32bit integer
  }
  return hash;
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
