import {Injectable} from '@angular/core';
import {AngularFireAuth} from "@angular/fire/compat/auth";
import {from, map, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private afAuth: AngularFireAuth) {
  }

  logout(): Observable<void> {
    return from(this.afAuth.signOut());
  }

  get isLoggedIn(): Observable<boolean> {
    return this.afAuth.user.pipe(map(user => user !== null));
  }

}
