import {Observable, of} from "rxjs";

export abstract class AuthenticationServiceAbstract {
  abstract isEmailVerified$: Observable<boolean>
  abstract isLoggedIn$: Observable<boolean>

  abstract logout(): void;

  abstract sendActivationEmail(): void;
}

export class AuthenticationServiceMock extends AuthenticationServiceAbstract {

  override isEmailVerified$: Observable<boolean> = of(true);
  override isLoggedIn$: Observable<boolean> = of(true);

  override logout(): void {
  }

  override sendActivationEmail(): void {
  }
}
