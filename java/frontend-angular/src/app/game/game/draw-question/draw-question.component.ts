import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {GameServiceAbstract} from "../../../services/game-service-abstract";

@Component({
  selector: 'app-draw-question',
  standalone: true,
  imports: [],
  templateUrl: './draw-question.component.html',
  styleUrl: './draw-question.component.css'
})
export class DrawQuestionComponent{
  @Input() gameId!: number;
  @Input() playerId!: string;

  constructor(protected gameService: GameServiceAbstract) {
  }
}
