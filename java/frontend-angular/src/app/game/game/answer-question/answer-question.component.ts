import {ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Game} from "../../game";
import {Player} from "../../../user/player";
import {NgIf} from "@angular/common";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {ValidationButtonComponent} from "../validation-button/validation-button.component";
import {AnswerQuestionResultsComponent} from "../answer-question-results-wrapper/answer-question-results/answer-question-results.component";
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
export class AnswerQuestionComponent extends Identifiable implements OnChanges{
  @Input() question!: QuestionDto
  @Input() game!: Game;
  @Input() player!: Player;
  protected canShowComponent!: boolean;

  constructor(private gameService: GameServiceAbstract,
              private cdr: ChangeDetectorRef) {
    super()
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
