import {Injectable} from '@angular/core';
import {AngularFireAuth} from "@angular/fire/compat/auth";
import {from, map, Observable} from "rxjs";
import {UserService} from "../../user/user.service";
import {AuthenticationServiceAbstract} from "../../authentication/authentication-service.abstract";

@Injectable({
  providedIn: 'root'
})
export class FirebaseAuthenticationService implements AuthenticationServiceAbstract {

  loggedIn: boolean = false;

  constructor(private afAuth: AngularFireAuth,
              private userService: UserService) {
    this.afAuth.onAuthStateChanged(auth => {
      this.loggedIn = auth !== null;
    })
  }

  logout(): Observable<void> {
    return from(this.afAuth.signOut().then(() => {
      this.userService.clearUser();
    }));
  }

  isLoggedIn(): Observable<boolean> {
    return this.afAuth.user.pipe(map(user => {
      return user !== null;
    }))
  }
}
