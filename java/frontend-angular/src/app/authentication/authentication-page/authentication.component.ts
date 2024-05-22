import {Component} from '@angular/core';
import {
  FirebaseuiAngularLibraryComponent,
  FirebaseUISignInFailure,
  FirebaseUISignInSuccessWithAuthResult
} from "firebaseui-angular";
import {Router} from "@angular/router";
import {AsyncPipe, NgIf} from "@angular/common";
import {AuthenticationService} from "../authentication.service";
import {UserService} from "../../user/user.service";
import {generateRandomString} from "../../common/helpers";
import {User} from "../../user/user";
import {ConsoleLogPipe} from "../../console-log.pipe";
import {AngularFireAuth} from "@angular/fire/compat/auth";

@Component({
  selector: 'app-authentication',
  standalone: true,
  imports: [
    FirebaseuiAngularLibraryComponent,
    NgIf,
    AsyncPipe,
    ConsoleLogPipe
  ],
  templateUrl: './authentication.component.html',
  styleUrl: './authentication.component.css'
})
export class AuthenticationComponent {

  constructor(protected authenticationService: AuthenticationService,
              protected userService: UserService,
              private router: Router) {
  }

  successCallback($event: FirebaseUISignInSuccessWithAuthResult) {
    let user = $event.authResult.user!;
    if (!user?.isAnonymous && user?.emailVerified === false) {
      // TODO stocker en base si un email a été envoyé à l'utilisateur
      // user.sendEmailVerification()
      this.router.navigate(['waiting-for-email-verification']);
    } else {
      let userNameUndefined: boolean = user.displayName == null
      let userName = user.displayName ?? 'anon-' + generateRandomString(6);
      let triviaUser = new User(user.uid,
        userName,
        user.isAnonymous);
      if(userNameUndefined){
        this.userService.updateUserName(userName)
      }
      this.userService.updateUser(triviaUser)
      this.router.navigate(['/games']);
    }
  }

  errorCallback($event: FirebaseUISignInFailure) {
  }

  uiShownCallback() {
  }
}
