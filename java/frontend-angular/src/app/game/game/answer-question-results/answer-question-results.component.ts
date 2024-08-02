import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges
} from '@angular/core';
import {ValidationButtonComponent} from "../validation-button/validation-button.component";
import {Player} from "../../../user/player";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-answer-question-results',
  standalone: true,
  imports: [
    ValidationButtonComponent,
    NgIf
  ],
  template: `
    <div>
      <div
        [attr.data-testid]="'is-answer-correct-prompt'"
      >
        <h2>{{ isAnswerCorrectPrompt }}</h2>
      </div>
      <p>{{ explanations }}</p>
      <app-validation-button [gameId]="gameId" [playerId]="player.id" [buttonText]="buttonText"/>
    </div>
  `,
  styleUrl: './answer-question-results.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AnswerQuestionResultsComponent implements OnChanges{
  @Input() gameId!: number;
  @Input() player!: Player;
  @Input() isAnswerCorrectPrompt!: string;
  @Input() explanations!: string;
  @Input() buttonText: string | undefined;

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['player']){
      this.player = changes['player'].currentValue;
    }
  }
}
