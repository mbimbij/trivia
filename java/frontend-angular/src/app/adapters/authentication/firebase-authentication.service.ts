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
  private activationEmailSendSubscription?: Subscription

  constructor(private afAuth: AngularFireAuth) {
    super();
  }

  ngOnDestroy(): void {
    this.activationEmailSendSubscription?.unsubscribe();
  }

  override sendActivationEmail(): void {
    this.activationEmailSendSubscription = this.afAuth.user
      .subscribe(user => user?.sendEmailVerification().then(() => {})
      );
  }

  override isEmailVerified(): Observable<boolean> {
    return this.afAuth.user
      .pipe(map(user => this.isUserAnonymousOrEmailVerified(user)))
  }

  private isUserAnonymousOrEmailVerified(user: firebase.User | null): boolean {
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
