import {User} from "../user/user";
import {Observable} from "rxjs";

export abstract class UserServiceAbstract {
  abstract renameUser(newUserName: string): void;
  abstract getUser(): Observable<User>;
}
