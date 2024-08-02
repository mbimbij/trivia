import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RollDiceResultsInsidePenaltyBoxComponent} from './roll-dice-results-inside-penalty-box.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {getMockGame2, getMockPlayer2} from "../../../common/test-helpers";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {GameService} from "../../game.service";
import {By} from "@angular/platform-browser";

describe('RollDiceBackhandInsidePenaltyBoxComponent', () => {
  let component: RollDiceResultsInsidePenaltyBoxComponent;
  let fixture: ComponentFixture<RollDiceResultsInsidePenaltyBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RollDiceResultsInsidePenaltyBoxComponent, HttpClientTestingModule],
      providers: [
        {provide: GameServiceAbstract, useClass: GameService},
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RollDiceResultsInsidePenaltyBoxComponent);
    component = fixture.componentInstance;
    component.game = getMockGame2()
    component.player = getMockPlayer2()
  });

  it('should create', () => {
    component.game = getMockGame2();
    component.player = getMockGame2().currentPlayer
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  const testCases = [
    {
      playerState: "WAITING_TO_VALIDATE_EVEN_DICE_ROLL_FROM_PENALTY_BOX",
      currentRoll: 4,
      expectedBackhandSectionShown: true,
      expectedBackHandMessage: "You rolled an even number: 4. You are getting out of the penalty box"
    },
    {
      playerState: "WAITING_TO_VALIDATE_ODD_DICE_ROLL_FROM_PENALTY_BOX",
      currentRoll: 3,
      expectedBackhandSectionShown: true,
      expectedBackHandMessage: "You rolled an odd number: 3. You stay in the penalty box"
    },
    {
      playerState: "ANY_OTHER_STATE",
      currentRoll: 3,
      expectedBackhandSectionShown: false,
      expectedBackHandMessage: "You should absolutely not see this message"
    },
  ]

  testCases.forEach(
    (
      {
        playerState,
        currentRoll,
        expectedBackhandSectionShown,
        expectedBackHandMessage},
      tcNum
    ) => {
      it(`GIVEN case number ${tcNum}, then should display or not backhand section after even roll from penalty box`,
        () => {
          // GIVEN
          component.player.state = playerState
          component.game.currentRoll = currentRoll
          component.updateAttributes()

          // WHEN
          fixture.detectChanges()

          // THEN
          let backhandElement = fixture.debugElement.query(By.css('[data-testid="roll-dice-results-section"]'));
          let backhandMessage = fixture.debugElement.query(By.css('[data-testid="roll-dice-results-message"]'));
          let backhandValidationButton = fixture.debugElement.query(By.css('[data-testid="roll-dice-results-validation"]'));
          if (expectedBackhandSectionShown) {
            expect(backhandElement).toBeTruthy()
            expect(backhandMessage.nativeElement.textContent.trim()).toEqual(expectedBackHandMessage)
            expect(backhandValidationButton).toBeTruthy()
          } else {
            expect(backhandElement).toBeFalsy()
            expect(backhandMessage).toBeFalsy()
            expect(backhandValidationButton).toBeFalsy()
          }
        }
      );
    }
  )
});
