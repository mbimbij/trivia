import {Injectable} from '@angular/core';
import {BehaviorSubject, map, Observable} from "rxjs";
import {Nobody, User} from "../../user/user";
import {AngularFireAuth} from "@angular/fire/compat/auth";
import {UserServiceAbstract} from "../../services/user-service.abstract";

@Injectable({
  providedIn: 'root'
})
export class FirebaseUserService extends UserServiceAbstract {
  userSubject = new BehaviorSubject<User>(Nobody.instance);

  constructor(private afAuth: AngularFireAuth) {
    super();
    this.updateUserSubject();
  }

  override getUser(): Observable<User> {
    return this.userSubject.asObservable()
  }

  override renameUser(newUserName: string): void {
    this.afAuth.user
      .subscribe(user => {
        user?.updateProfile({displayName: newUserName})
          .then(() => {
            this.updateUserSubject()
          })
      })
  }

  private updateUserSubject() {
    this.afAuth.user.pipe(map(user => this.buildDomainUser(user))).subscribe(
      value => {
        this.userSubject.next(value);
      }
    )
  }

  private buildDomainUser(user: firebase.default.User | null) {
    return user ? new User(user.uid, user.displayName!, user.isAnonymous) : Nobody.instance;
  }
}

