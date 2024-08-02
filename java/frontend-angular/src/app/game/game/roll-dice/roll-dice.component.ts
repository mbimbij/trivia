import {ChangeDetectionStrategy, Component, Input, OnChanges, OnDestroy, SimpleChanges} from '@angular/core';
import {Game} from "../../game";
import {Player} from "../../../user/player";
import {NgIf} from "@angular/common";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {mergeMap, of, Subscription} from "rxjs";
import {ValidationButtonComponent} from "../validation-button/validation-button.component";
import {
  RollDiceBackhandInsidePenaltyBoxComponent
} from "../roll-dice-backhand-inside-penalty-box/roll-dice-backhand-inside-penalty-box.component";
import {
  RollDiceBackhandOutsidePenaltyBoxComponent
} from "../roll-dice-backhand-outside-penalty-box/roll-dice-backhand-outside-penalty-box.component";

@Component({
  selector: 'app-roll-dice',
  standalone: true,
  imports: [
    NgIf,
    ValidationButtonComponent,
    RollDiceBackhandInsidePenaltyBoxComponent,
    RollDiceBackhandOutsidePenaltyBoxComponent
  ],
  templateUrl: `roll-dice.component.html`,
  styleUrl: './roll-dice.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RollDiceComponent implements OnChanges, OnDestroy {
  @Input() game!: Game;
  @Input() player!: Player;
  canRollDice: boolean | undefined;
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
  }
  rollDice() {
    this.subscription = this.gameService.rollDice(this.game.id, this.player.id)
      .subscribe();
  }
}
