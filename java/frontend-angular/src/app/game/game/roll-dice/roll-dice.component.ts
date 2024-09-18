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
  template: `
    <div>
      <button
        [attr.data-testid]="'roll-dice'"
        *ngIf="canRollDice || false"
        (click)="rollDice()"
      >
        roll dice
      </button>
    </div>
    {{ checkRender() }}
  `,
  styleUrl: './roll-dice.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RollDiceComponent extends Identifiable implements OnDestroy {
  @Input() gameId!: number;
  @Input() userId!: Player;
  @Input() canRollDice: boolean | undefined;
  private actionSubscription?: Subscription;

  constructor(protected gameService: GameServiceAbstract) {
    super()
  }

  ngOnDestroy(): void {
    this.actionSubscription?.unsubscribe();
  }

  rollDice() {
    this.actionSubscription = this.gameService.rollDice(this.gameId, this.userId.id)
      .subscribe();
  }
}
