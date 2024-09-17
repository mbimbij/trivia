import {ChangeDetectionStrategy, Component, Input, OnDestroy, SimpleChanges} from '@angular/core';
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {User} from "../../user/user";
import {Subscription} from "rxjs";
import {Game} from '../game';
import {compareUserAndPlayer} from "../../common/helpers";
import {Identifiable} from "../../common/identifiable";
import {State} from "../../openapi-generated/game";

@Component({
  selector: 'app-join-game-button',
  standalone: true,
  template: `
    @if (canJoin()) {
      <button [attr.data-testid]="'join-button-'+game.id" (click)="joinGame()">
        join
      </button>
    } @else if (isGameStarted()) {
      <span>{{ 'game started' }}</span>
    } @else if (isPlayerInGame()) {
      <span>{{ 'already joined' }}</span>
    } @else {
      <span>{{ 'cannot join' }}</span>
    }
    {{ checkRender() }}
  `,
  styleUrl: './join-game-button.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class JoinGameButtonComponent extends Identifiable implements OnDestroy {

  @Input() game!: Game
  @Input() user!: User;
  private joinActionSubscription?: Subscription;

  constructor(private gameService: GameServiceAbstract) {
    super()
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['user']) {
      this.user = changes['user'].currentValue;
    }
    if (changes['game']) {
      this.game = changes['game'].currentValue;
    }
  }

  ngOnDestroy(): void {
    this.joinActionSubscription?.unsubscribe();
  }

  canJoin(): boolean {
    // TODO empêcher cette fonction d'être appelée 36 fois
    // console.log(`canJoin called`)
    return !this.isPlayerInGame() && playersCountsLessThanMax(this.game) && !this.isGameStarted()

    function playersCountsLessThanMax(game: Game) {
      return game.players.length < 6;
    }
  }

  protected isPlayerInGame(): boolean {
    return this.game.players.some(player => compareUserAndPlayer(this.user, player));
  }

  protected isGameStarted(): boolean {
    return this.game.state === State.Started;
  }

  joinGame() {
    this.joinActionSubscription = this.gameService.join(this.game, this.user)
      .subscribe();
  }
}
