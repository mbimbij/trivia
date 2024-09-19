import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {AnswerQuestionResultsComponent} from "./answer-question-results/answer-question-results.component";
import {Identifiable} from "../../../common/identifiable";
import {State} from "../../../openapi-generated/game";
import {MessageService} from "../../../services/message.service";

@Component({
  selector: 'app-answer-question-results-wrapper',
  standalone: true,
  imports: [
    AnswerQuestionResultsComponent
  ],
  templateUrl: './answer-question-results-wrapper.component.html',
  styleUrl: './answer-question-results-wrapper.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AnswerQuestionResultsWrapperComponent extends Identifiable {
  @Input() gameId!: number;
  @Input() playerId!: string;
  @Input() isWaitingToValidateCorrectAnswer!: boolean;
  @Input() isWaitingToValidateFirstIncorrectAnswer!: boolean;
  @Input() isWaitingToValidateSecondIncorrectAnswer!: boolean;
  @Input() explanations: string | undefined;

  constructor(protected messageService: MessageService) {
    super();
  }

  protected readonly State = State;
}
