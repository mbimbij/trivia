import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {GameDetailsComponent} from "./game/game-details.component";
import {GameListComponent} from "./game-list/game-list.component";

const routes: Routes = [
  {path: "game/:id", component: GameDetailsComponent},
  {path: "", component: GameListComponent}
]

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
