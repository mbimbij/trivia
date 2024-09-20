import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GameListComponent} from './game-list.component';
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {CreateGameComponent} from "../create-game/create-game.component";
import {FormsModule} from "@angular/forms";
import {ActivatedRoute, provideRouter} from "@angular/router";
import {UserServiceMock} from "../../adapters/user/user-service.mock";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {AuthenticationServiceAbstract, AuthenticationServiceMock} from "../../services/authentication-service-abstract";
import {DebugElement} from "@angular/core";
import {of} from "rxjs";
import {getMockGame1, getMockGame2, getMockUser1} from "../../common/test-helpers";

const ROW_SELECTOR = '.game-row'

describe('GameListComponent', () => {
  let component: GameListComponent;
  let fixture: ComponentFixture<GameListComponent>;
  let htmlElement: HTMLElement;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        {provide: GameServiceAbstract, useClass: GameServiceMock},
        {provide: UserServiceAbstract, useClass: UserServiceMock},
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              user$: of(getMockUser1()),
              games$: of([getMockGame1(), getMockGame2()])
            })

          }
        }
      ],
      imports: [HttpClientTestingModule, CreateGameComponent, FormsModule],
    })
      .compileComponents();

    fixture = TestBed.createComponent(GameListComponent);
    component = fixture.componentInstance;
    htmlElement = fixture.nativeElement;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display games list', () => {
    fixture.detectChanges();

    fixture.whenStable().then(value => {
      let htmlGameRows = htmlElement.querySelectorAll(ROW_SELECTOR);
      expect(htmlGameRows).toHaveSize(2);
    })
  });
});
