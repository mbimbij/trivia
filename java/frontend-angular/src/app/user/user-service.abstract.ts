import {User} from "./user";

export abstract class UserServiceAbstract {
  abstract renameUser(newUserName: string): void;

  /**
   * Should only be called by Authentication Service (to be merged ?)
   * @param user
   */
  abstract setUser(user: User): void;

  abstract registerUserUpdatedObserver(observer: (updatedUser: User) => void): void;

  abstract getUser(): User;
}
