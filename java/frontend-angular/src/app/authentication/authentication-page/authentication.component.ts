import {Component} from '@angular/core';
import {
  FirebaseuiAngularLibraryComponent,
  FirebaseUISignInFailure,
  FirebaseUISignInSuccessWithAuthResult
} from "firebaseui-angular";
import {Router} from "@angular/router";
import {Observable} from "rxjs";
import {AsyncPipe, NgIf} from "@angular/common";
import {AuthenticationService} from "../authentication.service";
import {UserService} from "../../user/user.service";
import {generateRandomString, stringHashCode} from "../../common/helpers";

@Component({
  selector: 'app-authentication',
  standalone: true,
  imports: [
    FirebaseuiAngularLibraryComponent,
    NgIf,
    AsyncPipe
  ],
  templateUrl: './authentication.component.html',
  styleUrl: './authentication.component.css'
})
export class AuthenticationComponent {

  constructor(protected authenticationService: AuthenticationService,
              protected userService: UserService,
              private router: Router) {
  }

  logout() {
    this.authenticationService.logout();
  }

  get isLoggedIn(): Observable<boolean> {
    return this.authenticationService.isLoggedIn
  }

  successCallback($event: FirebaseUISignInSuccessWithAuthResult) {
    let user = $event.authResult.user!;
    if (!user?.isAnonymous && user?.emailVerified === false) {
      // TODO stocker en base si un email a été envoyé à l'utilisateur
      // user.sendEmailVerification()
      this.router.navigate(['waiting-for-email-verification']);
    } else {
      let triviaUser = {
        name: user.displayName ?? 'anon-' + generateRandomString(6),
        id: user.uid,
        idInteger: stringHashCode(user.uid)
      };
      this.userService.setUser(triviaUser)
      this.router.navigate(['games']);
    }
  }

  errorCallback($event: FirebaseUISignInFailure) {
  }

  uiShownCallback() {
  }
}
