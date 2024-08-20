import {Injectable, OnDestroy} from '@angular/core';
import {AngularFireAuth} from "@angular/fire/compat/auth";
import {from, map, Observable, Subscription} from "rxjs";
import {AuthenticationServiceAbstract} from "../../services/authentication-service.abstract";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import firebase from "firebase/compat";

@Injectable({
  providedIn: 'root'
})
export class FirebaseAuthenticationService extends AuthenticationServiceAbstract implements OnDestroy {
  override loggedIn: boolean = false;
  override emailVerified: boolean = false;
  private activationEmailSendSubscription?: Subscription

  constructor(private afAuth: AngularFireAuth) {
    super();
    this.afAuth.onAuthStateChanged(user => {
      this.loggedIn = user !== null;
      this.emailVerified = this.isEmailVerifiedInner(user);
    }).then(() => {})
  }

  ngOnDestroy(): void {
    this.activationEmailSendSubscription?.unsubscribe();
  }

  override sendActivationEmail(): void {
    this.activationEmailSendSubscription = this.afAuth.user
      .subscribe(user => user?.sendEmailVerification()
      );
  }

  override isEmailVerified(): Observable<boolean> {
    return this.afAuth.user.pipe(map(user => this.isEmailVerifiedInner(user)))
  }

  private isEmailVerifiedInner(user: firebase.User | null): boolean {
    return (user?.isAnonymous || user?.emailVerified) ?? false;
  }

  logout(): Observable<void> {
    return from(this.afAuth.signOut());
  }

  isLoggedIn(): Observable<boolean> {
    return this.afAuth.user.pipe(map(user => {
      return user !== null;
    }))
  }
}
