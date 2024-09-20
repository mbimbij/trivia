import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges
} from '@angular/core';
import {ValidationButtonComponent} from "../../validation-button/validation-button.component";
import {Player} from "../../../../user/player";
import {NgIf} from "@angular/common";
import {Identifiable} from "../../../../common/identifiable";

@Component({
  selector: 'app-answer-question-results',
  standalone: true,
  imports: [
    ValidationButtonComponent,
    NgIf
  ],
  template: `
    <div
      [attr.data-testid]="'answer-question-results'"
    >
      <div
        [attr.data-testid]="'is-answer-correct-prompt'"
      >
        <h2>{{ isAnswerCorrectPrompt }}</h2>
      </div>
      <p>{{ explanations }}</p>
      <app-validation-button [gameId]="gameId" [playerId]="playerId" [buttonText]="buttonText"/>
    </div>
    {{ checkRender() }}
  `,
  styleUrl: './answer-question-results.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AnswerQuestionResultsComponent extends Identifiable{
  @Input() gameId!: number;
  @Input() playerId!: string;
  @Input() isAnswerCorrectPrompt!: string;
  @Input() explanations!: string;
  @Input() buttonText: string | undefined;
}
