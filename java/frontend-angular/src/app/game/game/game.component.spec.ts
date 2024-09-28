import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GameComponent} from './game.component';
import {ActivatedRoute} from "@angular/router";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {getMockGame1, getMockGame2, getMockPlayer1, getMockUser2, MockActivatedRoute} from "../../common/test-helpers";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {UserServiceMock} from "../../adapters/user/user-service.mock";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {BehaviorSubject, of} from "rxjs";
import {GameService} from "../game.service";
import {HttpErrorResponse} from "@angular/common/http";
import {GameLogsServiceAbstract} from "../../services/gamelogs-service-abstract";
import {GameLogsService} from "../gamelogs.service";
import any = jasmine.any;

describe('GameComponent', () => {
  let component: GameComponent
  let fixture: ComponentFixture<GameComponent>
  let htmlElement: HTMLElement
  let gameService: GameServiceMock
  let userService: UserServiceMock;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameComponent, HttpClientTestingModule],
      providers: [
        {provide: ActivatedRoute, useClass: MockActivatedRoute},
        {provide: GameLogsServiceAbstract, useClass: GameLogsService},
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
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(gameService.getGame).toHaveBeenCalledWith(any(Number))
  });

  it('should display player action section if is current player', () => {
    // GIVEN
    component.setGame(getMockGame1())
    component.setPlayer(getMockPlayer1())

    // WHEN
    fixture.detectChanges()

    // THEN
    expect(htmlElement.querySelector(`[data-testid="player-action-section"]`)).toBeTruthy();
    expect(htmlElement.querySelector(`[data-testid="game-logs-section"]`)).toBeTruthy();
  });

  it('should NOT display player action section if is NOT current player', () => {
    // GIVEN
    component.setGame(getMockGame2())
    component.setPlayer(getMockPlayer1())

    // WHEN
    fixture.detectChanges()

    // THEN
    expect(htmlElement.querySelector(`[data-testid="player-action-section"]`)).toBeFalsy();
    expect(htmlElement.querySelector(`[data-testid="game-logs-section"]`)).toBeTruthy();
  });

  it('GIVEN current player BUT no dice roll THEN displays roll dice button', () => {
    // GIVEN
    let mockGame = getMockGame2();
    mockGame.currentRoll = undefined
    mockGame.currentQuestion = undefined
    mockGame.currentPlayer.state = "WAITING_FOR_DICE_ROLL"
    component.setGame(mockGame)

    // WHEN
    fixture.detectChanges()

    // THEN
    expect(htmlElement.querySelector(`[data-testid="roll-dice"]`)).toBeTruthy();
    expect(htmlElement.querySelector(`[data-testid="answer-question"]`)).toBeFalsy();
    expect(htmlElement.querySelector(`[data-testid="game-logs-section"]`)).toBeTruthy();
  });

});

describe('Error display', () => {
  let component: GameComponent;
  let htmlElement: HTMLElement;
  let fixture: ComponentFixture<GameComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameComponent, HttpClientTestingModule],
      providers: [
        {provide: ActivatedRoute, useClass: MockActivatedRoute},
        {provide: UserServiceAbstract, useClass: UserServiceMock},
        {provide: GameServiceAbstract, useClass: GameService},
        {provide: GameLogsServiceAbstract, useClass: GameLogsService},
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GameComponent);
    component = fixture.componentInstance;
    htmlElement = fixture.nativeElement;
  });

  it('game Should display game not found section, when http 404 returned', () => {
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
