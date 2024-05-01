import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {GameListComponent} from "./game-list/game-list.component";
import {GameDetailsComponent} from "./game-details/game-details.component";
import {GameComponent} from "./game/game.component";

const routes: Routes = [
  {path: "game/:id", component: GameComponent},
  {path: "game-details/:id", component: GameDetailsComponent},
  {path: "", component: GameListComponent}
]

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
