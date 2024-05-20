import {UserDto} from "./openapi-generated";
import {compareUserDto} from "./helpers";

describe('ExampleTest', () => {

  beforeEach(() => {
  });

  it('UserDto equality test', () => {
    let user1: UserDto = {id: 1, name: "name"}
    let user2: UserDto = {id: 1, name: "name"}
    let user3: UserDto = {id: 1, name: "name3"}
    let user4: UserDto = {id: 2, name: "name"}
    expect(compareUserDto(user1, user2)).toBeTrue();
    expect(compareUserDto(user1, user3)).toBeTrue();
    expect(compareUserDto(user1, user4)).toBeFalse();
  });
});
