import {ChangeDetectionStrategy, Component, Input, OnChanges, OnDestroy, SimpleChanges} from '@angular/core';
import {Game} from "../../game";
import {Player} from "../../../user/player";
import {NgIf} from "@angular/common";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {Subscription} from "rxjs";
import {Identifiable} from "../../../common/identifiable";

@Component({
  selector: 'app-roll-dice',
  standalone: true,
  imports: [
    NgIf,
  ],
  templateUrl: `roll-dice.component.html`,
  styleUrl: './roll-dice.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RollDiceComponent extends Identifiable implements OnChanges, OnDestroy {
  @Input() game!: Game;
  @Input() player!: Player;
  canRollDice: boolean | undefined;
  private subscription?: Subscription;

  constructor(protected gameService: GameServiceAbstract) {
    super()
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
