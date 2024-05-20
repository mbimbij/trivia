import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {GameListComponent} from "./game-list/game-list.component";
import {GameDetailsComponent} from "./game-details/game-details.component";
import {GameComponent} from "./game/game.component";
import {AuthenticationComponent} from "./auth/authentication.component";
import {
  WaitingForEmailVerificationComponent
} from "./waiting-for-email-verification/waiting-for-email-verification.component";
import {loginActivateGuard} from "./auth/login-activate.guard";

const routes: Routes = [
  {path: "games", component: GameListComponent, canActivate: [loginActivateGuard]},
  {path: "", component: GameListComponent, canActivate: [loginActivateGuard]},
  {path: "games/:id", component: GameComponent, canActivate: [loginActivateGuard]},
  {path: "games/:id/details", component: GameDetailsComponent, canActivate: [loginActivateGuard]},
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
