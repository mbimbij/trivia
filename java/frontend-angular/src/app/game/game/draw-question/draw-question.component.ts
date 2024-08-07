import {Component, Input} from '@angular/core';
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {Identifiable} from "../../../common/identifiable";

@Component({
  selector: 'app-draw-question',
  standalone: true,
  imports: [],
  template: `
    <button
      [attr.data-testid]="'draw-question'"
      (click)="this.gameService.drawQuestion(gameId, playerId).subscribe()">
      draw question
    </button>
  `,
  styleUrl: './draw-question.component.css'
})
export class DrawQuestionComponent extends Identifiable {
  @Input() gameId!: number;
  @Input() playerId!: string;

  constructor(protected gameService: GameServiceAbstract) {
    super()
  }
}
