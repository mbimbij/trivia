import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GameComponent} from './game.component';
import {ActivatedRoute, provideRouter, RouterModule} from "@angular/router";
import {GameServiceAbstract} from "../game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {Observable, of} from "rxjs";
import {mockGame1} from "../test-helpers";

describe('GameComponent', () => {
  let component: GameComponent;
  let fixture: ComponentFixture<GameComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        {provide: GameServiceAbstract, useClass: GameServiceMock},
        {provide: ActivatedRoute, useClass: MockActivatedRoute}
      ],
      imports: [GameComponent]
    })
      .compileComponents();
    const mockHistory = {
      state: mockGame1
    };
    Object.defineProperty(window, 'history', {value: mockHistory})

    fixture = TestBed.createComponent(GameComponent);
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
