import {Injectable} from '@angular/core';
import {Subject} from "rxjs";
import {mockUser1} from "../../common/test-helpers";
import {User} from "../../user/user";
import {AngularFireAuth} from "@angular/fire/compat/auth";
import {UserServiceAbstract} from "../../user/user-service.abstract";

@Injectable({
  providedIn: 'root'
})
export class FirebaseUserService extends UserServiceAbstract {
  private userSubject = new Subject<User>();
  private defaultUser: User = new User("1", "player-1", true);

  constructor(private afAuth: AngularFireAuth) {
    super();
  }

  renameUser(newUserName: string): void {
    this.afAuth.user
      .subscribe(user => {
        user?.updateProfile({displayName: newUserName})
          .then(() => {
            let updatedUser = this.getUser();
            updatedUser.name = newUserName
            this.setUser(updatedUser)
          })
      })
  }
  /**
   * Should only be called by Authentication Service (to be merged ?)
   * @param user
   */
  setUser(user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
    this.userSubject.next(user)
  }
  registerUserUpdatedObserver(observer: (updatedUser: User) => void) {
    this.userSubject.subscribe(observer)
  }
  getUser(): User {
    if (localStorage.getItem('user') == null) {
      localStorage.setItem('user', JSON.stringify(this.defaultUser))
    }
    return JSON.parse(localStorage.getItem('user')!)
  }
}

export class UserServiceTest extends FirebaseUserService {
  override getUser(): User {
    return mockUser1;
  }
}
