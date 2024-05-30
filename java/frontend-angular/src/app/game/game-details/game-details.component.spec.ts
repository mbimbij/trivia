import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GameDetailsComponent} from './game-details.component';
import {ActivatedRoute} from "@angular/router";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {MockActivatedRoute, mockGame1} from "../../common/test-helpers";
import {UserServiceMock} from "../../adapters/user/user-service.mock";
import {UserServiceAbstract} from "../../services/user-service.abstract";

describe('GameDetailsComponent', () => {
  let component: GameDetailsComponent;
  let fixture: ComponentFixture<GameDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        {provide: GameServiceAbstract, useClass: GameServiceMock},
        {provide: ActivatedRoute, useClass: MockActivatedRoute},
        {provide: UserServiceAbstract, useClass: UserServiceMock},
        {provide: GameServiceAbstract, useClass: GameServiceMock}
      ],
      imports: [GameDetailsComponent]
    })
      .compileComponents();

    // TODO delete state, as the backend is called
    Object.defineProperty(window, 'history', {
      value: {
        state: mockGame1
      }
    })

    fixture = TestBed.createComponent(GameDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});


