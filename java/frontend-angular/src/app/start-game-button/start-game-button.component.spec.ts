import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StartGameButtonComponent } from './start-game-button.component';
import {GameServiceAbstract} from "../game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {mockGame1} from "../test-helpers";

describe('StartGameButtonComponent', () => {
  let component: StartGameButtonComponent;
  let fixture: ComponentFixture<StartGameButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [{provide: GameServiceAbstract, useClass: GameServiceMock}],
      imports: [StartGameButtonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StartGameButtonComponent);
    component = fixture.componentInstance;
    component.game = mockGame1
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
