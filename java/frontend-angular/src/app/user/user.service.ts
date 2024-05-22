import {Injectable} from '@angular/core';
import {Subject} from "rxjs";
import {mockUser1} from "../common/test-helpers";
import {Nobody, User} from "./user";
import {AngularFireAuth} from "@angular/fire/compat/auth";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userSubject = new Subject<User>();
  private defaultUser = {id: 1, name: "player-1"};

  constructor(private afAuth: AngularFireAuth) {
  }

  updateUserName(newUserName: string): void {
    this.afAuth.user
      .subscribe(user => {
        user?.updateProfile({displayName: newUserName})
          .then(() => {
            let updatedUser = this.getUser();
            updatedUser.name = newUserName
            this.updateUser(updatedUser)
          })
      })
  }

  updateUser(user: User): void {
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

  clearUser() {
    this.updateUser(Nobody.instance);
  }
}

export class UserServiceTest extends UserService {
  override getUser(): User {
    return mockUser1;
  }
}
