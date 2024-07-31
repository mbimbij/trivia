import {ChangeDetectionStrategy, Component, Input, OnChanges, OnDestroy, SimpleChanges} from '@angular/core';
import {Game} from "../../game";
import {Player} from "../../../user/player";
import {NgIf} from "@angular/common";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {mergeMap, of, Subscription} from "rxjs";
import {ValidationButtonComponent} from "../validation-button/validation-button.component";

@Component({
  selector: 'app-roll-dice',
  standalone: true,
  imports: [
    NgIf,
    ValidationButtonComponent
  ],
  templateUrl: `roll-dice.component.html`,
  styleUrl: './roll-dice.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RollDiceComponent implements OnChanges, OnDestroy {
  @Input() game!: Game;
  @Input() player!: Player;
  canRollDice: boolean | undefined;
  canShowBackhand: boolean | undefined;
  backhandDisplayMessage: string | undefined;
  private subscription?: Subscription;

  constructor(protected gameService: GameServiceAbstract) {
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

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
    this.canRollDice = this.game.canRollDice(this.player)
    this.canShowBackhand = this.getCanShowBackhand()
    this.backhandDisplayMessage = this.getBackhandDisplayMessage()
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

  rollDice() {
    this.subscription = this.gameService.rollDice(this.game.id, this.player.id)
      .pipe(
        mergeMap(game => {
            if (game.canDrawQuestion(this.player)) {
              // console.log("coucou canDrawQuestion")
              return this.gameService.drawQuestion(game.id, this.player.id)
            }
            return of(game);
          }
        )
      )
      .subscribe();
  }
}
