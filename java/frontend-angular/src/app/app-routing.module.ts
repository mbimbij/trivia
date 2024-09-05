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
import { NotFoundComponent } from './common/not-found/not-found.component';
import {HealthComponent} from "./health/health.component";

const routes: Routes = [
  {path: "authentication", component: AuthenticationComponent},
  {
    path: "waiting-for-email-verification",
    component: WaitingForEmailVerificationComponent,
    canActivate: [loginActivateGuard]
  },
  {
    path: "games", component: GameListComponent
    , canActivate: [loginActivateGuard, emailVerifiedGuard]
  },
  {path: "", redirectTo: "/games", pathMatch: "full"},
  {path: "games/:id/details", component: GameDetailsComponent, canActivate: [loginActivateGuard, emailVerifiedGuard]},
  {path: "games/:id", component: GameComponent
    , canActivate: [loginActivateGuard, emailVerifiedGuard]
  },
  {path: 'not-found', component: NotFoundComponent},
  {path: 'health', component: HealthComponent},
  {path: '**', redirectTo: '/not-found'}
]

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes, {enableTracing: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
