import {ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Player} from "../../../user/player";
import {State} from "../../../openapi-generated";
import {AnswerQuestionResultsComponent} from "./answer-question-results/answer-question-results.component";

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
export class AnswerQuestionResultsWrapperComponent implements OnChanges{
  @Input() gameId!: number;
  @Input() player!: Player;
  @Input() explanations: string | undefined;

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['player']){
      this.player = changes['player'].currentValue;
    }
  }

  protected readonly State = State;
}
