import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RollDiceResultsOutsidePenaltyBoxComponent } from './roll-dice-results-outside-penalty-box.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {GameService} from "../../game.service";
import {getMockGame2} from "../../../common/test-helpers";

describe('RollDiceBackhandOutsidePenaltyBoxComponent', () => {
  let component: RollDiceResultsOutsidePenaltyBoxComponent;
  let fixture: ComponentFixture<RollDiceResultsOutsidePenaltyBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RollDiceResultsOutsidePenaltyBoxComponent, HttpClientTestingModule],
      providers: [
        {provide: GameServiceAbstract, useClass: GameService},
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RollDiceResultsOutsidePenaltyBoxComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    component.game = getMockGame2();
    component.player = getMockGame2().currentPlayer
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });
});
