import {Observable} from "rxjs";

export abstract class AuthenticationServiceAbstract {
  abstract loggedIn: boolean;
  abstract emailVerified: boolean;

  abstract logout(): Observable<void>;

  abstract isLoggedIn(): Observable<boolean>;

  abstract isEmailVerified(): Observable<boolean>;

  abstract sendActivationEmail(): void
}
