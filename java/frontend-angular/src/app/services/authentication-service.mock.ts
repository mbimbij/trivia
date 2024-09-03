import {Observable, of} from "rxjs";

export abstract class AuthenticationServiceAbstract {
  abstract isEmailVerified$: Observable<Boolean>
  abstract isLoggedIn$: Observable<Boolean>

  abstract logout(): void;

  abstract sendActivationEmail(): void
}

export class AuthenticationServiceMock extends AuthenticationServiceAbstract {
  override isEmailVerified$: Observable<Boolean> = of(true);
  override isLoggedIn$: Observable<Boolean> = of(true);

  override logout(): void {
  }

  override sendActivationEmail(): void {
  }
}
