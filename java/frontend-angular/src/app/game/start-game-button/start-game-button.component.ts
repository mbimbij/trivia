import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  OnDestroy,
} from '@angular/core';
import {NgIf} from "@angular/common";
import {User} from "../../user/user";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {compareUserAndPlayer, generateRandomString} from "../../common/helpers";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {Game} from "../game";
import {combineLatest, Subscription} from "rxjs";
import {Identifiable} from "../../common/identifiable";
import {State} from "../../openapi-generated/game";

@Component({
  standalone: true,
  selector: 'app-start-game-button',
  imports: [
    NgIf
  ],
  template: `
    <button [attr.data-testid]="'start-button-'+game.id" (click)="startGame()" *ngIf="canStartGameAttr">
      start
    </button>
    {{ checkRender() }}
  `,
  styleUrl: './start-game-button.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class StartGameButtonComponent extends Identifiable implements OnDestroy {

  @Input() game!: Game
  protected user!: User;
  protected canStartGameAttr: boolean = false

  constructor(private service: GameServiceAbstract,
              private userService: UserServiceAbstract,
              private gameService: GameServiceAbstract,
              private cdr: ChangeDetectorRef)  {
    super()
    // console.log(`constructor ${this.constructor.name} - ${this.id} called`)
  }

  private userGameSubscription: Subscription | undefined;
  private startActionSubscription: Subscription | undefined;

  ngOnInit() {
    // console.log(`ngOnInit ${this.constructor.name} - ${this.id} called`)
    let user$ = this.userService.getUser();
    let game$ = this.gameService.getGame(this.game.id);

    this.userGameSubscription = combineLatest([user$, game$])
      .subscribe(([user, game]) => {
        this.user = user;
        this.game = game;
        this.canStartGameAttr = this.canStartGame();
        this.cdr.markForCheck()
      });
  }

  ngOnDestroy() {
    // console.log(`ngOnDestroy ${this.constructor.name} - ${this.id} called`)
    this.userGameSubscription?.unsubscribe()
    this.startActionSubscription?.unsubscribe()
  }

  startGame() {
    this.startActionSubscription = this.service.start(this.game.id, this.user.id)
      .subscribe(() => {
        }
      )
  }

  private canStartGame(): boolean {
    return this.isPlayerCreator() && this.isGameJustCreated() && this.playersCountIsValid()
  }

  private playersCountIsValid() {
    return this.game.players.length >= 2 && this.game.players.length <= 6;
  }

  private isPlayerCreator(): boolean {
    return compareUserAndPlayer(this.user, this.game.creator);
  }

  private isGameJustCreated(): boolean {
    return this.game.state === State.Created
  }
}
