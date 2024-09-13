import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthenticationServiceAbstract} from "../../services/authentication-service-abstract";
import {map, skip} from "rxjs";

export const emailVerifiedGuard: CanActivateFn = (route, state) => {
  const authenticationService = inject(AuthenticationServiceAbstract);
  const router = inject(Router);

  return authenticationService.isEmailVerified()
    .pipe(
      map(isEmailVerified => {
        if (!isEmailVerified) {
          return router.createUrlTree(['waiting-for-email-verification']);
        }
        return true;
      })
    )
};
