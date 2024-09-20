import {ChangeDetectionStrategy, Component, Input, OnDestroy} from '@angular/core';
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {User} from "../../user/user";
import {Subscription} from "rxjs";
import {Identifiable} from "../../common/identifiable";

@Component({
  selector: 'app-join-game-button',
  standalone: true,
  template: `
    @if (canJoin) {
      <button [attr.data-testid]="'join-button-'+gameId" (click)="joinGame()">
        join
      </button>
    } @else if (isGameStarted) {
      <span>{{ 'game started' }}</span>
    } @else if (isPlayerInGame) {
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

  @Input() gameId!: number
  @Input() user!: User;
  @Input() canJoin!: boolean;
  @Input() isGameStarted!: boolean;
  @Input() isPlayerInGame!: boolean;
  private actionSubscription?: Subscription;

  constructor(private gameService: GameServiceAbstract) {
    super()
  }

  ngOnDestroy(): void {
    this.actionSubscription?.unsubscribe();
  }

  joinGame() {
    this.actionSubscription = this.gameService.join(this.gameId, this.user)
      .subscribe();
  }
}
