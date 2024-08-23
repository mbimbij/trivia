import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorDisplayComponent } from './error-display.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {ActivatedRoute} from "@angular/router";
import {MockActivatedRoute} from "../../common/test-helpers";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {UserServiceMock} from "../../adapters/user/user-service.mock";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {GameService} from "../game.service";
import {BehaviorSubject} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";

describe('ErrorDisplayComponent', () => {
  let fixture: ComponentFixture<ErrorDisplayComponent>;
  let component: ErrorDisplayComponent;
  let htmlElement: HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ErrorDisplayComponent, HttpClientTestingModule],
      providers: [
        {provide: ActivatedRoute, useClass: MockActivatedRoute},
        {provide: UserServiceAbstract, useClass: UserServiceMock},
        {provide: GameServiceAbstract, useClass: GameService},
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ErrorDisplayComponent);
    component = fixture.componentInstance;
    htmlElement = fixture.nativeElement;
  });

  it('error display Should display game not found section, when http 404 returned', () => {
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
