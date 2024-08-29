import {ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Game} from "../../game";
import {Player} from "../../../user/player";
import {DrawQuestionComponent} from "../draw-question/draw-question.component";
import {NgIf} from "@angular/common";
import {Identifiable} from "../../../common/identifiable";

@Component({
  selector: 'app-roll-dice-results-outside-penalty-box',
  standalone: true,
  imports: [
    DrawQuestionComponent,
    NgIf
  ],
  templateUrl: './roll-dice-results-outside-penalty-box.component.html',
  styleUrl: './roll-dice-results-outside-penalty-box.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RollDiceResultsOutsidePenaltyBoxComponent extends Identifiable implements OnChanges{
  @Input() game!: Game;
  @Input() player!: Player;
  canShowComponent: boolean | undefined;
  displayMessage: string | undefined;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['player']) {
      this.player = changes['player'].currentValue;
    }
    if (changes['game']) {
      this.game = changes['game'].currentValue;
    }
    this.updateAttributes();
  }

  updateAttributes() {
    this.canShowComponent = this.getCanShowBackhand()
    this.displayMessage = this.getBackhandDisplayMessage()
  }

  private getCanShowBackhand() {
    return new Set<string>([
      "WAITING_TO_DRAW_1ST_QUESTION"])
      .has(this.player.state);
  }

  private getBackhandDisplayMessage() {
    let prefix = !this.player.gotOutOfPenaltyBox ? `You rolled a ${this.game.currentRoll}. ` : ``;
    return prefix + `Your new location is ${this.player.location}. The category is ${this.game.currentCategory!}`
  }
}
