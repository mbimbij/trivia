import {TestBed} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {GameServiceMock} from "./game/game-service-mock";
import {GameServiceAbstract} from "./services/game-service-abstract";
import {CreateGameComponent} from "./game/create-game/create-game.component";
import {FormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";
import {NavbarComponent} from "./common/navbar/navbar.component";
import {UserServiceAbstract} from "./services/user-service.abstract";
import {UserServiceMock} from "./adapters/user/user-service.mock";
import {AuthenticationServiceAbstract, AuthenticationServiceMock} from "./services/authentication-service.mock";

describe('AppComponent', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      {provide: GameServiceAbstract, useClass: GameServiceMock},
      {provide: UserServiceAbstract, useClass: UserServiceMock},
      {provide: AuthenticationServiceAbstract, useClass: AuthenticationServiceMock},
    ],
    declarations: [AppComponent],
    imports: [ HttpClientTestingModule, CreateGameComponent, FormsModule, RouterModule, NavbarComponent ]
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'frontend-angular'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('frontend-angular');
  });

});
