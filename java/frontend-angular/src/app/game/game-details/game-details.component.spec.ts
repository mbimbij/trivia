import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GameDetailsComponent} from './game-details.component';
import {ActivatedRoute} from "@angular/router";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {MockActivatedRoute} from "../../common/test-helpers";
import {UserServiceMock} from "../../adapters/user/user-service.mock";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {GameService} from "../game.service";
import {BehaviorSubject} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";

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

    fixture = TestBed.createComponent(GameDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

describe('Error display', () => {
  let fixture: ComponentFixture<GameDetailsComponent>;
  let component: GameDetailsComponent;
  let htmlElement: HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameDetailsComponent, HttpClientTestingModule],
      providers: [
        {provide: ActivatedRoute, useClass: MockActivatedRoute},
        {provide: UserServiceAbstract, useClass: UserServiceMock},
        {provide: GameServiceAbstract, useClass: GameService},
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GameDetailsComponent);
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
