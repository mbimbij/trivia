import {ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Game} from "../../game";
import {Player} from "../../../user/player";
import {NgIf} from "@angular/common";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {flatMap, mergeMap, of, pipe} from "rxjs";

@Component({
  selector: 'app-roll-dice',
  standalone: true,
  imports: [
    NgIf
  ],
  template: `
    <button
      *ngIf="canRollDice || false"
      [attr.data-testid]="'roll-dice'"
      (click)="rollDice()"
    >
      roll dice
    </button>
  `,
  styleUrl: './roll-dice.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RollDiceComponent implements OnChanges {
  protected canRollDice: boolean | undefined;
  @Input() game!: Game;
  @Input() player!: Player;

  constructor(protected gameService: GameServiceAbstract) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['player']) {
      this.player = changes['player'].currentValue;
    }
    if (changes['game']) {
      this.game = changes['game'].currentValue;
    }
    this.canRollDice = this.game.canRollDice(this.player)
  }

  rollDice() {
    this.gameService.rollDice(this.game.id, this.player.id)
      .pipe(
        mergeMap(game => {
            if (game.canDrawQuestion(this.player)) {
              console.log("coucou canDrawQuestion")
              return this.gameService.drawQuestion(game.id, this.player.id)
            }
            return of(game);
          }
        )
      )
      .subscribe();
  }
}
