import {Observable, of} from "rxjs";

export abstract class AuthenticationServiceAbstract {
  abstract loggedIn: boolean;
  abstract emailVerified: boolean;

  abstract logout(): Observable<void>;

  abstract isLoggedIn(): Observable<boolean>;

  abstract isEmailVerified(): Observable<boolean>;

  abstract sendActivationEmail(): void
}

export class AuthenticationServiceMock extends AuthenticationServiceAbstract {
  override loggedIn: boolean = false;
  override emailVerified: boolean = false;

  override logout(): Observable<void> {
    this.loggedIn = false;
    this.emailVerified = false;
    return of();
  }

  override isLoggedIn(): Observable<boolean> {
    return of(this.loggedIn);
  }

  override isEmailVerified(): Observable<boolean> {
    return of(this.loggedIn);
  }

  override sendActivationEmail(): void {
  }
}
