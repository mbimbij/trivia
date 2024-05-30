import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {map} from "rxjs";
import {AuthenticationServiceAbstract} from "../../services/authentication-service.abstract";

export const loginActivateGuard: CanActivateFn = (route, state) => {
  const authenticationService = inject(AuthenticationServiceAbstract);
  const router = inject(Router);
  return authenticationService.isLoggedIn()
    .pipe(
      map(isLoggedIn => {
        if (!isLoggedIn) {
          return router.createUrlTree(['waiting-for-email-verification']);
        }
        return true;
      })
    )
};
