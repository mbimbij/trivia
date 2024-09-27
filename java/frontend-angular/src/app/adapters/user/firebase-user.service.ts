import {Injectable, OnDestroy} from '@angular/core';
import {Observable, ReplaySubject, Subscription} from "rxjs";
import {Nobody, User} from "../../user/user";
import {AngularFireAuth} from "@angular/fire/compat/auth";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import firebase from "firebase/compat";

@Injectable({
  providedIn: 'root'
})
export class FirebaseUserService extends UserServiceAbstract implements OnDestroy {
  private userSubject = new ReplaySubject<User>(1);
  private user!: User;
  private firebaseUser: firebase.User | null = null;
  private subscription?: Subscription;

  constructor(private afAuth: AngularFireAuth) {
    super();
    this.initService();
  }

  private initService() {
    this.afAuth.onAuthStateChanged(afUser => {
      this.firebaseUser = afUser
      this.user = this.buildDomainUser(afUser);
      this.userSubject.next(this.user)
    }).then(() => {
    })
  }

  private buildDomainUser(user: firebase.User | null) {
    return user ? new User(user.uid, user.displayName!, user.isAnonymous) : Nobody.instance;
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
        this.user = {...this.user,name: newUserName}
        // this.user.name = newUserName
        this.userSubject.next(this.user);
      })
  }
}

