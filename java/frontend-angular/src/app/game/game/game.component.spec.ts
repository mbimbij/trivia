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
import {GameService} from "../game.service";
import any = jasmine.any;

describe('GameComponent', () => {
  let component: GameComponent;
  let fixture: ComponentFixture<GameComponent>;
  let htmlElement: HTMLElement;
  let debugElement: DebugElement;
  let gameService: GameServiceAbstract;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameComponent, HttpClientTestingModule],
      providers: [
        {provide: ActivatedRoute, useClass: MockActivatedRoute},
        {provide: UserServiceAbstract, useClass: UserServiceMock},
      ]
    })
    .compileComponents();

    gameService = new GameServiceMock();
    spyOn(gameService, `getGame`).and.callThrough();

    TestBed.overrideProvider(GameServiceAbstract, { useValue: gameService });

    fixture = TestBed.createComponent(GameComponent);
    component = fixture.componentInstance;
    htmlElement = fixture.nativeElement;
    debugElement = fixture.debugElement;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(gameService.getGame).toHaveBeenCalledWith(any(Number))
  });

  it('should display expected elements eventually', () => {
    expect(htmlElement.querySelector(`[data-testid="player-action-section"]`)).toBeTruthy();
    expect(htmlElement.querySelector(`[data-testid="game-logs-section"]`)).toBeTruthy();
  });
});
