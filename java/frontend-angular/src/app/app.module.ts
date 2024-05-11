import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {HttpClientModule} from "@angular/common/http";
import {GameServiceAbstract} from "./game-service-abstract";
import {GameService} from "./game.service";
import {ApiModule} from "./openapi-generated";
import {CreateGameComponent} from "./create-game/create-game.component";
import {FormsModule} from "@angular/forms";
import {AppRoutingModule} from './app-routing.module';
import {GameListComponent} from "./game-list/game-list.component";
import {RxStompService} from "./rx-stomp.service";
import {rxStompServiceFactory} from "./rx-stomp-service-factory";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ApiModule,
    CreateGameComponent,
    FormsModule,
    AppRoutingModule,
    GameListComponent
  ],
  providers: [
    {provide: GameServiceAbstract, useClass: GameService},
    {provide: RxStompService, useFactory: rxStompServiceFactory}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
