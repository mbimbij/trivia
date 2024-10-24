import {ChangeDetectionStrategy, Component, Input, OnDestroy} from '@angular/core';
import {NgIf} from "@angular/common";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {Subscription} from "rxjs";
import {Identifiable} from "../../../shared/identifiable";

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
  @Input() userId!: string;
  private actionSubscription?: Subscription;

  constructor(protected gameService: GameServiceAbstract) {
    super()
  }

  ngOnDestroy(): void {
    this.actionSubscription?.unsubscribe();
  }

  rollDice() {
    this.actionSubscription = this.gameService.rollDice(this.gameId, this.userId)
      .subscribe();
  }
}
