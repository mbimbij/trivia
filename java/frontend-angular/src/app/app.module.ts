import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {HttpClientModule} from "@angular/common/http";
import {GameServiceAbstract} from "./services/game-service-abstract";
import {GameService} from "./game/game.service";
import {ApiModule, BASE_PATH, Configuration} from "./openapi-generated";
import {CreateGameComponent} from "./game/create-game/create-game.component";
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
import {AuthenticationServiceAbstract} from "./services/authentication-service.abstract";
import {FirebaseAuthenticationService} from "./adapters/authentication/firebase-authentication.service";
import {FirebaseUserService} from "./adapters/user/firebase-user.service";
import {UserServiceAbstract} from "./services/user-service.abstract";
import {NavbarComponent} from "./game/navbar/navbar.component";

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
        FirebaseUIModule.forRoot(firebaseUiAuthConfig),
        NavbarComponent
    ],
  providers: [
    {provide: GameServiceAbstract, useClass: GameService},
    {provide: AuthenticationServiceAbstract, useClass: FirebaseAuthenticationService},
    {provide: UserServiceAbstract, useClass: FirebaseUserService},
    {provide: RxStompService, useFactory: rxStompServiceFactory},
    {provide: BASE_PATH, useValue: environment.backendUrl},
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
