import {UserDto} from "./openapi-generated";

export function compareUserDto(user1: UserDto, user2: UserDto): boolean {
  return user1 !== null && user2 !== null && user1.constructor === user2.constructor && user1.id === user2.id && user1.name === user2.name;
}
