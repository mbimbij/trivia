import {Injectable, OnDestroy} from '@angular/core';
import {AngularFireAuth} from "@angular/fire/compat/auth";
import {BehaviorSubject, from, map, mergeMap, Observable, Subscription} from "rxjs";
import {AuthenticationServiceAbstract} from "../../services/authentication-service.mock";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import firebase from "firebase/compat";
import {FirebaseUserService} from "../user/firebase-user.service";
import {fromPromise} from "rxjs/internal/observable/innerFrom";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class FirebaseAuthenticationService extends AuthenticationServiceAbstract implements OnDestroy {
  private isLoggedInSubject = new BehaviorSubject(false);
  override isLoggedIn$: Observable<boolean> = this.isLoggedInSubject.asObservable();
  private isEmailVerifiedSubject = new BehaviorSubject(false);
  override isEmailVerified$: Observable<boolean> = this.isEmailVerifiedSubject.asObservable()
  private activationEmailSendSubscription?: Subscription

  constructor(private afAuth: AngularFireAuth,
              private router: Router) {
    super();
    this.initService();
  }

  private initService() {
    this.afAuth.onAuthStateChanged(afUser => {
      this.isLoggedInSubject.next(afUser !== null)
      this.isEmailVerifiedSubject.next(this.isUserAnonymousOrEmailVerified(afUser))
    })
  }

  ngOnDestroy(): void {
    this.activationEmailSendSubscription?.unsubscribe();
  }

  override sendActivationEmail(): void {
    this.activationEmailSendSubscription = this.afAuth.user
      .subscribe(user => user?.sendEmailVerification().then(() => {
        })
      );
  }

  private isUserAnonymousOrEmailVerified(user: firebase.User | null): boolean {
    return (user?.isAnonymous || user?.emailVerified) ?? false;
  }

  logout() {
    this.afAuth.signOut()
      .then(value => this.router.navigate(['/authentication']))
  }
}
