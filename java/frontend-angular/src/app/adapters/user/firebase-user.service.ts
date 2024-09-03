import {Injectable, OnDestroy} from '@angular/core';
import {BehaviorSubject, map, Observable, Subscription} from "rxjs";
import {Nobody, User} from "../../user/user";
import {AngularFireAuth} from "@angular/fire/compat/auth";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {generateRandomString} from "../../common/helpers";
import firebase from "firebase/compat";

@Injectable({
  providedIn: 'root'
})
export class FirebaseUserService extends UserServiceAbstract implements OnDestroy {
  userSubject = new BehaviorSubject<User>(Nobody.instance);
  private firebaseUser: firebase.User | null = null;
  private subscription?: Subscription;

  constructor(private afAuth: AngularFireAuth) {
    super();
    this.initUserSubject();
  }

  private initUserSubject() {
    this.afAuth.onAuthStateChanged(afUser => {
      this.firebaseUser = afUser
      this.userSubject.next(this.buildDomainUser(afUser))
    }).then(() => {
    })
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  override getUser(): Observable<User> {
    return this.userSubject.asObservable()
  }

  override renameUser(newUserName: string): void {
    this.firebaseUser?.updateProfile({displayName: newUserName})
      .then(() => {
        let newUser = this.userSubject.value;
        newUser.name = newUserName
        this.userSubject.next(newUser);
      })
  }

  private buildDomainUser(user: firebase.User | null) {
    return user ? new User(user.uid, user.displayName!, user.isAnonymous) : Nobody.instance;
  }
}

