import {Injectable} from '@angular/core';
import {Subject} from "rxjs";
import {mockUser1} from "../common/test-helpers";
import {User} from "./user";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userSubject = new Subject<User>();
  private defaultUser = {id: 1, name: "player-1"};

  constructor() {
  }

  setUser(user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
    this.userSubject.next(user)
  }

  registerUserUpdatedObserver(observer: (newPlayerName: User) => void) {
    this.userSubject.subscribe(observer)
  }

  getUser(): User {
    if (localStorage.getItem('user') == null) {
      localStorage.setItem('user', JSON.stringify(this.defaultUser))
    }
    return JSON.parse(localStorage.getItem('user')!)
  }
}

export class LocalStorageServiceTest extends UserService {
  override getUser(): User {
    return mockUser1;
  }
}
