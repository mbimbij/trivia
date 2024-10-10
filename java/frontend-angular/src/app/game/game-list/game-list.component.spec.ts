import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GameListComponent} from './game-list.component';
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {OpenCreateGameComponent} from "../create-game/open-create-game.component";
import {FormsModule} from "@angular/forms";
import {provideRouter} from "@angular/router";
import {UserServiceMock} from "../../adapters/user/user-service.mock";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {AuthenticationServiceAbstract, AuthenticationServiceMock} from "../../services/authentication-service-abstract";
import {DebugElement} from "@angular/core";

describe('GameListComponent', () => {
  let component: GameListComponent;
  let fixture: ComponentFixture<GameListComponent>;
  let htmlElement: HTMLElement;
  let debugElement: DebugElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        {provide: GameServiceAbstract, useClass: GameServiceMock},
        {provide: UserServiceAbstract, useClass: UserServiceMock},
        {provide: AuthenticationServiceAbstract, useClass: AuthenticationServiceMock},
        provideRouter([])
      ],
      imports: [HttpClientTestingModule, OpenCreateGameComponent, FormsModule],
    })
      .compileComponents();

    fixture = TestBed.createComponent(GameListComponent);
    component = fixture.componentInstance;
    htmlElement = fixture.nativeElement;
    debugElement = fixture.debugElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display games list', () => {
    let htmlGameRows = htmlElement.querySelectorAll('.game-row');
    expect(htmlGameRows).toHaveSize(2);
  });
});
