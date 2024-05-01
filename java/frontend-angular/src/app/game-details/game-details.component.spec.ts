import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GameDetailsComponent} from './game-details.component';
import {ActivatedRoute, provideRouter, RouterModule} from "@angular/router";
import {GameServiceAbstract} from "../game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {Observable, of} from "rxjs";
import {mockGame1} from "../test-helpers";

describe('GameComponent', () => {
  let component: GameDetailsComponent;
  let fixture: ComponentFixture<GameDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        {provide: GameServiceAbstract, useClass: GameServiceMock},
        {provide: ActivatedRoute, useClass: MockActivatedRoute}
      ],
      imports: [GameDetailsComponent]
    })
      .compileComponents();
    const mockHistory = {
      state: mockGame1
    };
    Object.defineProperty(window, 'history', {value: mockHistory})

    fixture = TestBed.createComponent(GameDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});


class MockActivatedRoute {
  params: Observable<any> = of({id: '123'}); // Mock route params
}
