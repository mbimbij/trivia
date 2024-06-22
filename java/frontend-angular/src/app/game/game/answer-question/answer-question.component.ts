import {Component, Input, SimpleChanges} from '@angular/core';
import {QuestionDto} from "../../../openapi-generated";
import {Game} from "../../game";
import {Player} from "../../../user/player";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-answer-question',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './answer-question.component.html',
  styleUrl: './answer-question.component.css'
})
export class AnswerQuestionComponent {
  @Input() question!: QuestionDto
  @Input() game!: Game;
  @Input() player!: Player;
  protected canAnswerQuestion!: boolean;

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['player']){
      this.player = changes['player'].currentValue;
    }
    if(changes['game']){
      this.game = changes['game'].currentValue;
    }
    this.canAnswerQuestion = this.game.canAnswerQuestion(this.player)
  }
}
