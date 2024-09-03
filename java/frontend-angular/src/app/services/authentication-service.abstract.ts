import {Observable, of} from "rxjs";

export abstract class AuthenticationServiceAbstract {

  abstract logout(): Observable<void>;

  abstract isLoggedIn(): Observable<boolean>;

  abstract isEmailVerified(): Observable<boolean>;

  abstract sendActivationEmail(): void
}

export class AuthenticationServiceMock extends AuthenticationServiceAbstract {

  override logout(): Observable<void> {
    return of();
  }

  override isLoggedIn(): Observable<boolean> {
    return of(true);
  }

  override isEmailVerified(): Observable<boolean> {
    return of(true);
  }

  override sendActivationEmail(): void {
  }
}
