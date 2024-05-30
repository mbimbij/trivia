import {Injectable} from '@angular/core';
import {AngularFireAuth} from "@angular/fire/compat/auth";
import {from, map, Observable} from "rxjs";
import {AuthenticationServiceAbstract} from "../../services/authentication-service.abstract";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import firebase from "firebase/compat";

@Injectable({
  providedIn: 'root'
})
export class FirebaseAuthenticationService extends AuthenticationServiceAbstract {
  override loggedIn: boolean = false;
  override emailVerified: boolean = false;

  constructor(private afAuth: AngularFireAuth,
              private userService: UserServiceAbstract) {
    super();
    this.afAuth.onAuthStateChanged(user => {
      this.loggedIn = user !== null;
      this.emailVerified =  this.isEmailVerifiedInner(user) ;
    })
  }

  override sendActivationEmail(): void {
    this.afAuth.user
      .subscribe(user => user?.sendEmailVerification()
      )
  }

  override isEmailVerified(): Observable<boolean> {
    return this.afAuth.user.pipe(map(user => this.isEmailVerifiedInner(user)))
  }

  private isEmailVerifiedInner(user: firebase.User | null): boolean{
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
