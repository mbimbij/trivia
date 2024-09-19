import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {NgIf} from "@angular/common";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {ValidationButtonComponent} from "../validation-button/validation-button.component";
import {
  AnswerQuestionResultsComponent
} from "../answer-question-results-wrapper/answer-question-results/answer-question-results.component";
import {Identifiable} from "../../../common/identifiable";
import {AnswerCode, QuestionDto} from "../../../openapi-generated/game";

@Component({
  selector: 'app-answer-question',
  standalone: true,
  imports: [
    NgIf,
    ValidationButtonComponent,
    AnswerQuestionResultsComponent
  ],
  templateUrl: './answer-question.component.html',
  styleUrl: './answer-question.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AnswerQuestionComponent extends Identifiable {
  @Input() question!: QuestionDto
  @Input() gameId!: number;
  @Input() playerId!: string;

  constructor(private gameService: GameServiceAbstract) {
    super()
  }

  protected answerQuestion(answerCode: AnswerCode): void {
    this.gameService.answerQuestion(this.gameId, this.playerId, answerCode).subscribe()
  }

  protected readonly AnswerCode = AnswerCode;
}
