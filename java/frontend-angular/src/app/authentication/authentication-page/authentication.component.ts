import {ChangeDetectionStrategy, Component} from '@angular/core';
import {
  FirebaseuiAngularLibraryComponent,
  FirebaseUISignInFailure,
  FirebaseUISignInSuccessWithAuthResult
} from "firebaseui-angular";
import {Router} from "@angular/router";
import {AsyncPipe, NgIf} from "@angular/common";
import {generateRandomString} from "../../common/helpers";
import {ConsoleLogPipe} from "../../console-log.pipe";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {FirebaseAuthenticationService} from "../../adapters/authentication/firebase-authentication.service";
import {Identifiable} from "../../common/identifiable";
import {Observable} from "rxjs";

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
export class AuthenticationComponent extends Identifiable{
  protected isLoggedIn$: Observable<boolean>

  constructor(protected authenticationService: FirebaseAuthenticationService,
              protected userService: UserServiceAbstract,
              protected router: Router) {
    super()
    this.isLoggedIn$ = this.authenticationService.isLoggedIn();
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
