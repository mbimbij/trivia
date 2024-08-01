import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RollDiceBackhandOutsidePenaltyBoxComponent } from './roll-dice-backhand-outside-penalty-box.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {GameServiceAbstract} from "../../../../services/game-service-abstract";
import {GameService} from "../../../game.service";
import {getMockGame2} from "../../../../common/test-helpers";

describe('RollDiceBackhandOutsidePenaltyBoxComponent', () => {
  let component: RollDiceBackhandOutsidePenaltyBoxComponent;
  let fixture: ComponentFixture<RollDiceBackhandOutsidePenaltyBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RollDiceBackhandOutsidePenaltyBoxComponent, HttpClientTestingModule],
      providers: [
        {provide: GameServiceAbstract, useClass: GameService},
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RollDiceBackhandOutsidePenaltyBoxComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    component.game = getMockGame2();
    component.player = getMockGame2().currentPlayer
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });
});
