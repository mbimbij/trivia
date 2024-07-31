import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GameComponent} from './game.component';
import {ActivatedRoute} from "@angular/router";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {
  getMockGame2,
  getMockPlayer1,
  getMockQuestion1,
  getMockUser2,
  MockActivatedRoute
} from "../../common/test-helpers";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {UserServiceMock} from "../../adapters/user/user-service.mock";
import {ChangeDetectorRef, DebugElement} from "@angular/core";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {BehaviorSubject, of} from "rxjs";
import {GameService} from "../game.service";
import {HttpErrorResponse} from "@angular/common/http";
import any = jasmine.any;

describe('GameComponent', () => {
  let component: GameComponent;
  let fixture: ComponentFixture<GameComponent>;
  let htmlElement: HTMLElement;
  let debugElement: DebugElement;
  let gameService: GameServiceMock;
  let userService: UserServiceMock;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameComponent, HttpClientTestingModule],
      providers: [
        {provide: ActivatedRoute, useClass: MockActivatedRoute},
      ]
    })
      .compileComponents();

    gameService = new GameServiceMock();
    spyOn(gameService, `getGame`).and.returnValue(of(getMockGame2()));

    userService = new UserServiceMock();
    spyOn(userService, `getUser`).and.returnValue(of(getMockUser2()));

    TestBed.overrideProvider(GameServiceAbstract, {useValue: gameService});
    TestBed.overrideProvider(UserServiceAbstract, {useValue: userService});

    fixture = TestBed.createComponent(GameComponent);
    component = fixture.componentInstance;
    htmlElement = fixture.nativeElement;
    debugElement = fixture.debugElement;

    fixture.autoDetectChanges(true);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(gameService.getGame).toHaveBeenCalledWith(any(Number))
  });

  it('should display expected elements eventually', () => {
    expect(htmlElement.querySelector(`[data-testid="game-header-section"]`)).toBeTruthy();
    expect(htmlElement.querySelector(`[data-testid="game-logs-section"]`)).toBeTruthy();
  });

  it('should display player action section if is current player', () => {
    expect(htmlElement.querySelector(`[data-testid="player-action-section"]`)).toBeTruthy();
    expect(htmlElement.querySelector(`[data-testid="game-logs-section"]`)).toBeTruthy();
  });

  it('should NOT display player action section if is NOT current player', () => {
    component.setPlayer(getMockPlayer1())
    fixture.componentRef.injector.get(ChangeDetectorRef).detectChanges()
    expect(htmlElement.querySelector(`[data-testid="player-action-section"]`)).toBeFalsy();
    expect(htmlElement.querySelector(`[data-testid="game-logs-section"]`)).toBeTruthy();
  });

  it('GIVEN current player BUT no dice roll THEN displays roll dice button', () => {
    let mockGame = getMockGame2();
    mockGame.currentRoll = undefined
    mockGame.currentQuestion = undefined
    mockGame.currentPlayer.state = "WAITING_FOR_DICE_ROLL"
    component.setGame(mockGame)
    fixture.componentRef.injector.get(ChangeDetectorRef).detectChanges()
    expect(htmlElement.querySelector(`[data-testid="roll-dice"]`)).toBeTruthy();
    expect(htmlElement.querySelector(`[data-testid="answer-question"]`)).toBeFalsy();
    expect(htmlElement.querySelector(`[data-testid="game-logs-section"]`)).toBeTruthy();
  });

  it('GIVEN current player AND dice roll THEN does not displays roll dice button', () => {
    let mockGame = getMockGame2();
    mockGame.currentRoll = 3
    mockGame.currentQuestion = undefined
    component.setGame(mockGame)
    fixture.componentRef.injector.get(ChangeDetectorRef).detectChanges()
    expect(htmlElement.querySelector(`[data-testid="roll-dice"]`)).toBeFalsy();
    expect(htmlElement.querySelector(`[data-testid="answer-question"]`)).toBeFalsy();
    expect(htmlElement.querySelector(`[data-testid="game-logs-section"]`)).toBeTruthy();
  });

  it('GIVEN current player AND dice roll THEN does not displays roll dice button', () => {
    let mockGame = getMockGame2();
    mockGame.currentRoll = 3
    mockGame.currentQuestion = getMockQuestion1()
    component.setGame(mockGame)
    fixture.componentRef.injector.get(ChangeDetectorRef).detectChanges()
    expect(htmlElement.querySelector(`[data-testid="roll-dice"]`)).toBeFalsy();
    expect(htmlElement.querySelector(`[data-testid="answer-question"]`)).toBeTruthy();
    expect(htmlElement.querySelector(`[data-testid="game-logs-section"]`)).toBeTruthy();
  });
});

describe('Error display', () => {
  let fixture: ComponentFixture<GameComponent>;
  let component: GameComponent;
  let htmlElement: HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameComponent, HttpClientTestingModule],
      providers: [
        {provide: ActivatedRoute, useClass: MockActivatedRoute},
        {provide: UserServiceAbstract, useClass: UserServiceMock},
        {provide: GameServiceAbstract, useClass: GameService},
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GameComponent);
    component = fixture.componentInstance;
    htmlElement = fixture.nativeElement;
  });

  it('Should display game not found section, when http 404 returned', () => {
    component.gameLoadingError$ = new BehaviorSubject<HttpErrorResponse>(new HttpErrorResponse({
      status: 404,
      error: {message: `some message`}
    }));

    fixture.detectChanges()

    expect(component).toBeTruthy()
    expect(htmlElement.querySelector('[data-testid=ok-section]')).toBeFalsy()
    expect(htmlElement.querySelector('[data-testid=error-section]')).toBeTruthy()
    expect(htmlElement.querySelector('[data-testid=loading-section]')).toBeFalsy()
    expect(htmlElement.querySelector('[data-testid=game-not-found-section]')).toBeTruthy()
    expect(htmlElement.querySelector('[data-testid=other-error-section]')).toBeFalsy()
  });

  it('Should display other error section, when http 400 returned', () => {
    component.gameLoadingError$ = new BehaviorSubject<HttpErrorResponse>(new HttpErrorResponse({
      status: 400,
      error: {message: `some message`}
    }));

    fixture.detectChanges()

    expect(component).toBeTruthy()
    expect(htmlElement.querySelector('[data-testid=ok-section]')).toBeFalsy()
    expect(htmlElement.querySelector('[data-testid=error-section]')).toBeTruthy()
    expect(htmlElement.querySelector('[data-testid=loading-section]')).toBeFalsy()
    expect(htmlElement.querySelector('[data-testid=game-not-found-section]')).toBeFalsy()
    expect(htmlElement.querySelector('[data-testid=other-error-section]')).toBeTruthy()
  });

  it('Should display other error section, when http 500 returned', () => {
    component.gameLoadingError$ = new BehaviorSubject<HttpErrorResponse>(new HttpErrorResponse({
      status: 500,
      error: {message: `some message`}
    }));

    fixture.detectChanges()

    expect(component).toBeTruthy()
    expect(htmlElement.querySelector('[data-testid=ok-section]')).toBeFalsy()
    expect(htmlElement.querySelector('[data-testid=error-section]')).toBeTruthy()
    expect(htmlElement.querySelector('[data-testid=loading-section]')).toBeFalsy()
    expect(htmlElement.querySelector('[data-testid=game-not-found-section]')).toBeFalsy()
    expect(htmlElement.querySelector('[data-testid=other-error-section]')).toBeTruthy()
  });

  it('Should display loading section, when http 500 returned', () => {
    component.gameLoadingError$ = new BehaviorSubject<HttpErrorResponse>(new HttpErrorResponse({
      status: 500,
      error: {message: `some message`}
    }));

    fixture.detectChanges()

    expect(component).toBeTruthy()
    expect(htmlElement.querySelector('[data-testid=ok-section]')).toBeFalsy()
    expect(htmlElement.querySelector('[data-testid=error-section]')).toBeTruthy()
    expect(htmlElement.querySelector('[data-testid=loading-section]')).toBeFalsy()
    expect(htmlElement.querySelector('[data-testid=game-not-found-section]')).toBeFalsy()
    expect(htmlElement.querySelector('[data-testid=other-error-section]')).toBeTruthy()
  });

});
