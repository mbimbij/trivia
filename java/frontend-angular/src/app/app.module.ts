import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {HttpClientModule} from "@angular/common/http";
import {GameServiceAbstract} from "./services/game-service-abstract";
import {GameService} from "./game/game.service";
import {OpenCreateGameComponent} from "./game/create-game/open-create-game.component";
import {FormsModule} from "@angular/forms";
import {AppRoutingModule} from './app-routing.module';
import {GameListComponent} from "./game/game-list/game-list.component";
import {RxStompService} from "./adapters/websockets/rx-stomp.service";
import {rxStompServiceFactory} from "./adapters/websockets/rx-stomp-service-factory";
import {getAuth, provideAuth} from '@angular/fire/auth';
import {AngularFireModule} from "@angular/fire/compat";
import {environment} from "../environments/environment";
import {AngularFireAuthModule, USE_EMULATOR as USE_AUTH_EMULATOR} from "@angular/fire/compat/auth";
import {FirebaseUIModule} from "firebaseui-angular";
import {firebaseUiAuthConfig} from "./adapters/authentication/firebase-ui-auth-config";
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {AuthenticationServiceAbstract} from "./services/authentication-service-abstract";
import {FirebaseAuthenticationService} from "./adapters/authentication/firebase-authentication.service";
import {FirebaseUserService} from "./adapters/user/firebase-user.service";
import {UserServiceAbstract} from "./services/user-service.abstract";
import {NavbarComponent} from "./common/navbar/navbar.component";
import {ApiModule as GameApiModule, BASE_PATH as GAME_API_BASE_PATH} from "./openapi-generated/game";
import {ApiModule as GameLogsApiModule, BASE_PATH as GAMELOGS_API_BASE_PATH} from "./openapi-generated/gamelogs";
import {GameLogsServiceAbstract} from "./services/gamelogs-service-abstract";
import {GameLogsService} from "./game/gamelogs.service";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    GameApiModule,
    GameLogsApiModule,
    OpenCreateGameComponent,
    FormsModule,
    AppRoutingModule,
    GameListComponent,
    AngularFireModule.initializeApp(environment.firebaseConfig),
    AngularFireAuthModule,
    FirebaseUIModule.forRoot(firebaseUiAuthConfig),
    NavbarComponent
  ],
  providers: [
    {provide: GameServiceAbstract, useClass: GameService},
    {provide: GameLogsServiceAbstract, useClass: GameLogsService},
    {provide: AuthenticationServiceAbstract, useClass: FirebaseAuthenticationService},
    {provide: UserServiceAbstract, useClass: FirebaseUserService},
    {provide: RxStompService, useFactory: rxStompServiceFactory},
    {provide: GAME_API_BASE_PATH, useValue: environment.backendUrl},
    {provide: GAMELOGS_API_BASE_PATH, useValue: environment.backendUrl},
    provideAuth(() => getAuth()),
    {
      provide: USE_AUTH_EMULATOR,
      useValue: undefined
      // useValue: !environment.production ? ['http://localhost:9099'] : undefined
    },
    provideAnimationsAsync(),
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
