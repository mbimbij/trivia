import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AnswerQuestionResultsComponent} from './answer-question-results.component';
import {getMockPlayer1} from "../../../../common/test-helpers";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {GameServiceAbstract} from "../../../../services/game-service-abstract";
import {GameService} from "../../../game.service";

describe('AnswerQuestionResultsComponent', () => {
  let component: AnswerQuestionResultsComponent;
  let fixture: ComponentFixture<AnswerQuestionResultsComponent>;

  let VALIDATION_BUTTON_SELECTOR = '[data-testid="validate"]'

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnswerQuestionResultsComponent, HttpClientTestingModule],
      providers: [
        {provide: GameServiceAbstract, useClass: GameService},
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnswerQuestionResultsComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    // GIVEN
    component.playerId = getMockPlayer1().id

    // WHEN
    fixture.detectChanges()

    // THEN
    expect(component).toBeTruthy();
  });

  it('should display "ok" on validation button if no text provided', () => {
    // GIVEN
    component.playerId = getMockPlayer1().id
    component.buttonText = undefined

    // WHEN
    fixture.detectChanges()

    // THEN
    let textContent = fixture.nativeElement.querySelector(VALIDATION_BUTTON_SELECTOR).textContent.trim();
    expect(textContent).toEqual("ok")
  });
});
