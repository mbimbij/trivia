import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {HttpClientModule} from "@angular/common/http";
import {GameServiceAbstract} from "./game-service-abstract";
import {GameService} from "./game.service";
import {GameServiceMock} from "./game-service-mock";
import {ApiModule} from "./openapi-generated";
import {CreateGameComponent} from "./create-game/create-game.component";
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent
  ],
    imports: [
        BrowserModule,
        HttpClientModule,
        ApiModule,
        CreateGameComponent,
        FormsModule
    ],
  providers: [{
    provide: GameServiceAbstract, useClass: GameService
  }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
