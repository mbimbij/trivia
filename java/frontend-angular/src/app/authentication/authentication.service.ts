import {Injectable} from '@angular/core';
import {AngularFireAuth} from "@angular/fire/compat/auth";
import {from, map, Observable, Subject} from "rxjs";
import {UserService} from "../user/user.service";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  loggedIn : boolean = false;

  constructor(private afAuth: AngularFireAuth,
              private userService: UserService) {
    this.afAuth.onAuthStateChanged(auth => {
      if(auth !== null){
        this.loggedIn = true
      }else{
        this.loggedIn = false
      }
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
