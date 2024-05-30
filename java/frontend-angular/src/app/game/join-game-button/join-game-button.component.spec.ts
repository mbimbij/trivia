import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JoinGameButtonComponent } from './join-game-button.component';
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {UserServiceMock} from "../../adapters/user/user-service.mock";
import {mockGame1} from "../../common/test-helpers";

describe('JoinGameButtonComponent', () => {
  let component: JoinGameButtonComponent;
  let fixture: ComponentFixture<JoinGameButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        {provide: GameServiceAbstract, useClass: GameServiceMock},
        {provide: UserServiceAbstract, useClass: UserServiceMock},
      ],
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

  // TODO écrire un test vérifiant que le composant disparaît / devient grisé quand un 6e joueur rejoint la partie
});
