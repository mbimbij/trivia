import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {Identifiable} from "../../../common/identifiable";

@Component({
  selector: 'app-draw-question',
  standalone: true,
  imports: [],
  template: `
    <button
      [attr.data-testid]="'draw-question'"
      (click)="drawQuestion()">
      draw question
    </button>
    {{ checkRender() }}
  `,
  styleUrl: './draw-question.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DrawQuestionComponent extends Identifiable {
  @Input() gameId!: number;
  @Input() playerId!: string;

  constructor(protected gameService: GameServiceAbstract) {
    super()
  }

  protected drawQuestion(){
    this.gameService.drawQuestion(this.gameId, this.playerId).subscribe()
  }
}
