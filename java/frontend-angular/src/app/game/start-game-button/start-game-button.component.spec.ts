import {ComponentFixture, TestBed} from '@angular/core/testing';

import {StartGameButtonComponent} from './start-game-button.component';
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {getMockGame1} from "../../common/test-helpers";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {UserServiceMock} from "../../adapters/user/user-service.mock";

describe('StartGameButtonComponent', () => {
  let component: StartGameButtonComponent;
  let fixture: ComponentFixture<StartGameButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        {provide: GameServiceAbstract, useClass: GameServiceMock},
        {provide: UserServiceAbstract, useClass: UserServiceMock},
      ],
      imports: [StartGameButtonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StartGameButtonComponent);
    component = fixture.componentInstance;
    component.game = getMockGame1()
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
