import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RollDiceComponent} from './roll-dice.component';
import {mockGame2, mockPlayer2} from "../../../common/test-helpers";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {GameServiceMock} from "../../game-service-mock";

describe('RollDiceComponent', () => {
  let component: RollDiceComponent;
  let fixture: ComponentFixture<RollDiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RollDiceComponent],
      providers: [
        {provide: GameServiceAbstract, useClass: GameServiceMock},
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RollDiceComponent);
    component = fixture.componentInstance;
    component.game = mockGame2
    component.player = mockPlayer2
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
