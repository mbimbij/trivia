import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RollDiceComponent} from './roll-dice.component';
import {getMockGame2, getMockPlayer2} from "../../../common/test-helpers";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {GameServiceMock} from "../../game-service-mock";
import {By} from "@angular/platform-browser";

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
      component.player = getMockPlayer2()
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

  });
