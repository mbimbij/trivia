import {Component} from '@angular/core';
import {
  FirebaseuiAngularLibraryComponent,
  FirebaseUISignInFailure,
  FirebaseUISignInSuccessWithAuthResult
} from "firebaseui-angular";
import {Router} from "@angular/router";
import {AsyncPipe, NgIf} from "@angular/common";
import {generateRandomString} from "../../common/helpers";
import {ConsoleLogPipe} from "../../console-log.pipe";
import {AuthenticationServiceAbstract} from "../../services/authentication-service.abstract";
import {UserServiceAbstract} from "../../services/user-service.abstract";

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

  constructor(protected authenticationService: AuthenticationServiceAbstract,
              protected userService: UserServiceAbstract,
              private router: Router) {
  }

  successCallback($event: FirebaseUISignInSuccessWithAuthResult) {
    let user = $event.authResult.user!;
    if (!user?.isAnonymous && user?.emailVerified === false) {
      // TODO stocker en base si un email a été envoyé à l'utilisateur
      // user.sendEmailVerification()
      this.router.navigate(['waiting-for-email-verification']);
    } else {
      let userName = user.displayName ?? generateName(user.isAnonymous);
      if(user.displayName == null){
        this.userService.renameUser(userName)
      }
      this.router.navigate(['/games']);
    }

    function generateName(isAnonymous: boolean): string{
      let hash = generateRandomString(6);
      return isAnonymous ? `anon-${hash}` : `user-${hash}`;
    }
  }

  errorCallback($event: FirebaseUISignInFailure) {
  }

  uiShownCallback() {
  }
}
