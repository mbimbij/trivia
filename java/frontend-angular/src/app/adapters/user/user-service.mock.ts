import {UserServiceAbstract} from "../../services/user-service.abstract";
import {Observable, of} from "rxjs";
import {User} from "src/app/user/user";
import {mockUser1} from "../../common/test-helpers";

export class UserServiceMock extends UserServiceAbstract {
  user: User = mockUser1;

  override renameUser(newUserName: string): void {
    this.user.name = newUserName;
  }

  override getUser(): Observable<User> {
    return of(this.user);
  }
}
