import {ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, SimpleChanges} from '@angular/core';
import {AnswerCode, QuestionDto} from "../../../openapi-generated";
import {Game} from "../../game";
import {Player} from "../../../user/player";
import {NgIf} from "@angular/common";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {ValidationButtonComponent} from "../validation-button/validation-button.component";
import {AnswerQuestionResultsComponent} from "../answer-question-results/answer-question-results.component";

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
export class AnswerQuestionComponent {
  @Input() question!: QuestionDto
  @Input() game!: Game;
  @Input() player!: Player;
  protected canShowComponent!: boolean;

  constructor(private gameService: GameServiceAbstract,
              private cdr: ChangeDetectorRef) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['player']){
      this.player = changes['player'].currentValue;
    }
    if(changes['game']){
      this.game = changes['game'].currentValue;
    }
    this.updateAttributes();
    this.cdr.markForCheck()
  }

  private updateAttributes() {
    this.canShowComponent = this.game.canAnswerQuestion(this.player)
  }

  protected answerQuestion(gameId: number, playerId: string, answerCode: AnswerCode): void {
    this.gameService.answerQuestion(gameId, playerId, answerCode).subscribe()
  }
  protected readonly AnswerCode = AnswerCode;
}
