import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GameDetailsComponent} from './game-details.component';
import {ActivatedRoute} from "@angular/router";
import {GameServiceAbstract} from "../game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {MockActivatedRoute, mockGame1} from "../../common/test-helpers";
import {UserServiceTest} from "../../adapters/user/user-service.test";
import {UserServiceAbstract} from "../../user/user-service.abstract";

describe('GameDetailsComponent', () => {
  let component: GameDetailsComponent;
  let fixture: ComponentFixture<GameDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        {provide: GameServiceAbstract, useClass: GameServiceMock},
        {provide: ActivatedRoute, useClass: MockActivatedRoute},
        {provide: UserServiceAbstract, useClass: UserServiceTest},
        {provide: GameServiceAbstract, useClass: GameServiceMock}
      ],
      imports: [GameDetailsComponent]
    })
      .compileComponents();

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


