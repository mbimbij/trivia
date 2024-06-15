import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GameComponent} from './game.component';
import {ActivatedRoute} from "@angular/router";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MockActivatedRoute, mockGame1} from "../../common/test-helpers";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {UserServiceMock} from "../../adapters/user/user-service.mock";
import {DebugElement} from "@angular/core";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {GameServiceMock} from "../game-service-mock";

describe('GameComponent', () => {
  let component: GameComponent;
  let fixture: ComponentFixture<GameComponent>;
  let htmlElement: HTMLElement;
  let debugElement: DebugElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameComponent, HttpClientTestingModule],
      providers: [
        {provide: ActivatedRoute, useClass: MockActivatedRoute},
        {provide: UserServiceAbstract, useClass: UserServiceMock},
        {provide: GameServiceAbstract, useClass: GameServiceMock},
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GameComponent);
    component = fixture.componentInstance;
    htmlElement = fixture.nativeElement;
    debugElement = fixture.debugElement;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display expected elements eventually', () => {
    expect(htmlElement.querySelector(`[data-testid="player-action-section"]`)).toBeTruthy();
  });
});
