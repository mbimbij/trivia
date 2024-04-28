import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JoinGameButtonComponent } from './join-game-button.component';
import {GameServiceAbstract} from "../game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {mockGame1} from "../test-helpers";

describe('JoinGameButtonComponent', () => {
  let component: JoinGameButtonComponent;
  let fixture: ComponentFixture<JoinGameButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [{provide: GameServiceAbstract, useClass: GameServiceMock}],
      imports: [JoinGameButtonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JoinGameButtonComponent);
    component = fixture.componentInstance;
    component.game = mockGame1
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
