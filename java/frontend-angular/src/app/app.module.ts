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
import { initializeApp, provideFirebaseApp } from '@angular/fire/app';
import { getAuth, provideAuth } from '@angular/fire/auth';
import {AngularFireModule} from "@angular/fire/compat";
import {environment} from "../environments/environment";
import {AngularFireAuthModule} from "@angular/fire/compat/auth";
import {firebase, firebaseui, FirebaseUIModule} from "firebaseui-angular";
import {USE_EMULATOR as USE_AUTH_EMULATOR} from "@angular/fire/compat/auth/auth";

const firebaseUiAuthConfig: firebaseui.auth.Config = {
  signInFlow: 'popup',
  signInOptions: [
    // firebase.auth.GoogleAuthProvider.PROVIDER_ID,
    // {
    //   scopes: [
    //     'public_profile',
    //     'email',
    //     'user_likes',
    //     'user_friends'
    //   ],
    //   customParameters: {
    //     'auth_type': 'reauthenticate'
    //   },
    //   provider: firebase.auth.FacebookAuthProvider.PROVIDER_ID
    // },
    // firebase.auth.TwitterAuthProvider.PROVIDER_ID,
    // firebase.auth.GithubAuthProvider.PROVIDER_ID,
    {
      requireDisplayName: false,
      provider: firebase.auth.EmailAuthProvider.PROVIDER_ID
    },
    // firebase.auth.PhoneAuthProvider.PROVIDER_ID,
    firebaseui.auth.AnonymousAuthProvider.PROVIDER_ID
  ],
  tosUrl: '<your-tos-link>',
  privacyPolicyUrl: '<your-privacyPolicyUrl-link>',
  // credentialHelper: firebaseui.auth.CredentialHelper.GOOGLE_YOLO
};

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
    provideFirebaseApp(() => initializeApp({"projectId":"trivia-fc64f","appId":"1:237690080555:web:bd06ce66b131659d9e096e","storageBucket":"trivia-fc64f.appspot.com","apiKey":"AIzaSyBgVgToHnITb86RRrQYX7-s2vWJoa3IDEw","authDomain":"trivia-fc64f.firebaseapp.com","messagingSenderId":"237690080555","measurementId":"G-NBMCMLL4JD"})),
    provideAuth(() => getAuth()),
    // {provide: USE_AUTH_EMULATOR, useValue: !environment.production ? ['localhost', 9099] : undefined}
    {provide: USE_AUTH_EMULATOR, useValue: !environment.production ? ['localhost', 9099] : undefined}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
