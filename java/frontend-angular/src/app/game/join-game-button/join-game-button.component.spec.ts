import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JoinGameButtonComponent } from './join-game-button.component';
import {GameServiceAbstract} from "../game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {mockGame1} from "../test-helpers";
import {UserService, LocalStorageServiceTest} from "../user.service";

describe('JoinGameButtonComponent', () => {
  let component: JoinGameButtonComponent;
  let fixture: ComponentFixture<JoinGameButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        {provide: GameServiceAbstract, useClass: GameServiceMock},
        {provide: UserService, useClass: LocalStorageServiceTest},
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