import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {GameListComponent} from "./game/game-list/game-list.component";
import {GameDetailsComponent} from "./game/game-details/game-details.component";
import {GameComponent} from "./game/game/game.component";
import {AuthenticationComponent} from "./authentication/authentication-page/authentication.component";
import {
  WaitingForEmailVerificationComponent
} from "./authentication/waiting-for-email-verification/waiting-for-email-verification.component";
import {loginActivateGuard} from "./authentication/authentication-guard/login-activate.guard";
import {emailVerifiedGuard} from "./authentication/authentication-guard/email-verified.guard";

const routes: Routes = [
  {path: "games", component: GameListComponent
    , canActivate: [loginActivateGuard, emailVerifiedGuard]
  },
  {path: "", redirectTo: "/games", pathMatch: "full"},
  {path: "games/:id", component: GameComponent, canActivate: [loginActivateGuard, emailVerifiedGuard]},
  {path: "games/:id/details", component: GameDetailsComponent, canActivate: [loginActivateGuard, emailVerifiedGuard]},
  {path: "authentication", component: AuthenticationComponent},
  {
    path: "waiting-for-email-verification",
    component: WaitingForEmailVerificationComponent,
    canActivate: [loginActivateGuard]
  }
]

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
