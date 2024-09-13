import {Observable, of} from "rxjs";

export abstract class AuthenticationServiceAbstract {
  abstract isLoggedIn(): Observable<boolean>
  abstract isEmailVerified(): Observable<boolean>

  abstract sendActivationEmail(): void;

  abstract logout(): void;
}

export class AuthenticationServiceMock extends AuthenticationServiceAbstract {
  override isLoggedIn(): Observable<boolean> {
    return of(true);
  }

  override isEmailVerified(): Observable<boolean> {
    return of(true);
  }

  override sendActivationEmail(): void {
  }

  override logout(): void {
  }
}
