import {Injectable, OnDestroy} from '@angular/core';
import {AngularFireAuth} from "@angular/fire/compat/auth";
import {Observable, ReplaySubject, Subscription} from "rxjs";
import {AuthenticationServiceAbstract} from "../../services/authentication-service-abstract";
import firebase from "firebase/compat";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class FirebaseAuthenticationService extends AuthenticationServiceAbstract implements OnDestroy {
  private isLoggedInSubject = new ReplaySubject<boolean>(1);
  override isLoggedIn$: Observable<boolean> = this.isLoggedInSubject.asObservable();
  private isEmailVerifiedSubject = new ReplaySubject<boolean>(1);
  override isEmailVerified$: Observable<boolean> = this.isEmailVerifiedSubject.asObservable()
  private afUser: firebase.User | null = null;
  private subscription?: Subscription;

  constructor(private afAuth: AngularFireAuth,
              private router: Router) {
    super();
    this.initService();
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe()
  }

  private initService() {
    this.afAuth.onAuthStateChanged(afUser => {
      this.afUser = afUser;
      this.isLoggedInSubject.next(afUser !== null)
      this.isEmailVerifiedSubject.next(this.isUserAnonymousOrEmailVerified(afUser))
    })
  }

  override sendActivationEmail(): void {
    this.afUser?.sendEmailVerification().then();
  }

  private isUserAnonymousOrEmailVerified(user: firebase.User | null): boolean {
    return (user?.isAnonymous || user?.emailVerified) ?? false;
  }

  override logout() {
    this.afAuth.signOut()
      .then(value => this.router.navigate(['/authentication']))
  }
}
