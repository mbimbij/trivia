import {ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Player} from "../../../../user/player";
import {Game} from "../../../game";
import {ValidationButtonComponent} from "../../validation-button/validation-button.component";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-roll-dice-backhand-inside-penalty-box',
  standalone: true,
  imports: [
    ValidationButtonComponent,
    NgIf
  ],
  templateUrl: './roll-dice-backhand-inside-penalty-box.component.html',
  styleUrl: './roll-dice-backhand-inside-penalty-box.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RollDiceBackhandInsidePenaltyBoxComponent implements OnChanges{
  @Input() game!: Game;
  @Input() player!: Player;
  canShowComponent: boolean | undefined;
  displayMessage: string | undefined;

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['player']){
      this.player = changes['player'].currentValue;
    }
    if(changes['game']){
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
      "WAITING_TO_VALIDATE_EVEN_DICE_ROLL_FROM_PENALTY_BOX",
      "WAITING_TO_VALIDATE_ODD_DICE_ROLL_FROM_PENALTY_BOX"])
      .has(this.player.state);
  }
  private getBackhandDisplayMessage() {
    if(this.player.state === "WAITING_TO_VALIDATE_EVEN_DICE_ROLL_FROM_PENALTY_BOX"){
      return `You rolled an even number: ${this.game.currentRoll}. You are getting out of the penalty box`
    } else if(this.player.state === "WAITING_TO_VALIDATE_ODD_DICE_ROLL_FROM_PENALTY_BOX"){
      return `You rolled an odd number: ${this.game.currentRoll}. You stay in the penalty box`
    } else {
      return `You should absolutely not see this message`
    }
  }
}
