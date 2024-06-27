import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnswerQuestionComponent } from './answer-question.component';
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {GameServiceMock} from "../../game-service-mock";

describe('AnswerQuestionComponent', () => {
  let component: AnswerQuestionComponent;
  let fixture: ComponentFixture<AnswerQuestionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnswerQuestionComponent],
      providers: [
        {provide: GameServiceAbstract, useClass: GameServiceMock},
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnswerQuestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
