import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {map, tap} from "rxjs";
import {AuthenticationService} from "../authentication.service";
import {log} from "@angular-devkit/build-angular/src/builders/ssr-dev-server";

export const loginActivateGuard: CanActivateFn = (route, state) => {
  const authenticationService = inject(AuthenticationService);
  const router = inject(Router);
  return authenticationService.isLoggedIn()
    .pipe(
      map(isLoggedIn => {
        if (!isLoggedIn) {
          return router.createUrlTree(['authentication']);
        }
        return true;
      })
    )
};
