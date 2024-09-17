import {ChangeDetectionStrategy, Component, Input, OnChanges, OnDestroy, SimpleChanges,} from '@angular/core';
import {AsyncPipe, NgIf} from "@angular/common";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {Game} from "../game";
import {BehaviorSubject, Subscription} from "rxjs";
import {Identifiable} from "../../common/identifiable";
import {State} from "../../openapi-generated/game";

@Component({
  standalone: true,
  selector: 'app-start-game-button',
  imports: [
    NgIf,
    AsyncPipe
  ],
  template: `
    <button [attr.data-testid]="'start-button-'+game.id" (click)="startGame()" *ngIf="(canStartGameSubject | async)">
      start
    </button>
    {{ checkRender() }}
  `,
  styleUrl: './start-game-button.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class StartGameButtonComponent extends Identifiable implements OnChanges, OnDestroy {

  @Input() game!: Game
  @Input() userId!: string;
  protected canStartGameSubject = new BehaviorSubject(false)

  constructor(private gameService: GameServiceAbstract) {
    super()
  }

  private startActionSubscription: Subscription | undefined;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['userId']) {
      this.userId = changes['userId'].currentValue;
    }
    if (changes['game']) {
      this.game = changes['game'].currentValue;
    }
    this.canStartGameSubject.next(this.canStartGame(this.game, this.userId));
  }

  ngOnDestroy() {
    this.startActionSubscription?.unsubscribe()
  }

  private canStartGame(game: Game, userId: string): boolean {
    return isPlayerCreator(game, userId) && isGameJustCreated(game) && playersCountIsValid(game)

    function isPlayerCreator(game: Game, userId: string): boolean {
      return userId == game.creator.id;
    }

    function isGameJustCreated(game: Game): boolean {
      return game.state === State.Created
    }

    function playersCountIsValid(game: Game) {
      return game.players.length >= 2 && game.players.length <= 6;
    }
  }

  startGame() {
    this.startActionSubscription = this.gameService.start(this.game.id, this.userId)
      .subscribe()
  }
}
