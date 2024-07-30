import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RollDiceComponent} from './roll-dice.component';
import {getMockGame2, mockGame2, mockPlayer2} from "../../../common/test-helpers";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {GameServiceMock} from "../../game-service-mock";
import {By} from "@angular/platform-browser";
import {ChangeDetectorRef} from "@angular/core";

describe('RollDiceComponent',
  () => {
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
      component.game = getMockGame2()
      component.player = mockPlayer2
    });

    it('should create', () => {
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
            let backhandElement = fixture.debugElement.query(By.css('[data-testid="roll-dice-backhand-section"]'));
            let backhandMessage = fixture.debugElement.query(By.css('[data-testid="roll-dice-backhand-message"]'));
            let backhandValidationButton = fixture.debugElement.query(By.css('[data-testid="roll-dice-backhand-validation"]'));
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

    // it('should display backhand section after even roll from penalty box', () => {
    //   // GIVEN
    //   component.player.state = "WAITING_TO_VALIDATE_EVEN_DICE_ROLL_FROM_PENALTY_BOX"
    //   component.canShowBackhand = component.getCanShowBackhand()
    //
    //   // WHEN
    //   fixture.detectChanges()
    //
    //   // THEN
    //   let backhandElement = fixture.debugElement.query(By.css('[data-testid="roll-dice-backhand"]'));
    //   expect(backhandElement).toBeTruthy()
    //
    // });
  });
