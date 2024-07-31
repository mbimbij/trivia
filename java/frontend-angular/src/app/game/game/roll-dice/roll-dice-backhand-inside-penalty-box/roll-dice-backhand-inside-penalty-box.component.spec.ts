import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RollDiceBackhandInsidePenaltyBoxComponent} from './roll-dice-backhand-inside-penalty-box.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {getMockGame2} from "../../../../common/test-helpers";
import {GameServiceAbstract} from "../../../../services/game-service-abstract";
import {GameService} from "../../../game.service";

describe('RollDiceBackhandInsidePenaltyBoxComponent', () => {
  let component: RollDiceBackhandInsidePenaltyBoxComponent;
  let fixture: ComponentFixture<RollDiceBackhandInsidePenaltyBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RollDiceBackhandInsidePenaltyBoxComponent, HttpClientTestingModule],
      providers: [
        {provide: GameServiceAbstract, useClass: GameService},
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RollDiceBackhandInsidePenaltyBoxComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    component.game = getMockGame2();
    component.player = getMockGame2().currentPlayer
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });
});
