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
import {getAuth, provideAuth} from '@angular/fire/auth';
import {AngularFireModule} from "@angular/fire/compat";
import {environment} from "../environments/environment";
import {AngularFireAuthModule, USE_EMULATOR as USE_AUTH_EMULATOR} from "@angular/fire/compat/auth";
import {FirebaseUIModule} from "firebaseui-angular";
import {firebaseUiAuthConfig} from "./firebase-ui-auth-config";
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

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
    GameListComponent,
    AngularFireModule.initializeApp(environment.firebaseConfig),
    AngularFireAuthModule,
    FirebaseUIModule.forRoot(firebaseUiAuthConfig)
  ],
  providers: [
    {provide: GameServiceAbstract, useClass: GameService},
    {provide: RxStompService, useFactory: rxStompServiceFactory},
    provideAuth(() => getAuth()),
    {
      provide: USE_AUTH_EMULATOR,
      useValue:  undefined
      // useValue: !environment.production ? ['http://localhost:9099'] : undefined
    },
    provideAnimationsAsync(),
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
