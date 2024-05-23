import {Observable} from "rxjs";

export abstract class AuthenticationServiceAbstract {
  abstract loggedIn: boolean;

  abstract logout(): Observable<void>;

  abstract isLoggedIn(): Observable<boolean>;
}
