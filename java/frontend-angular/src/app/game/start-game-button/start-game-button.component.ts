import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {NgIf} from "@angular/common";
import {User} from "../../user/user";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {compareUserAndPlayer} from "../../common/helpers";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {combineLatest, combineLatestAll, combineLatestWith} from "rxjs";
import {Game} from "../game";

@Component({
  standalone: true,
  selector: 'app-start-game-button',
  imports: [
    NgIf
  ],
  template: `
    <button (click)="startGame()" *ngIf="canStartGameAttr">
      start
    </button>
  `,
  styleUrl: './start-game-button.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class StartGameButtonComponent {

  @Input() game!: Game
  protected user!: User;
  protected canStartGameAttr: boolean = false

  constructor(private service: GameServiceAbstract,
              private userService: UserServiceAbstract,
              private gameService: GameServiceAbstract) {
    console.log(`constructor ${this.constructor.name} called`)
  }

  ngOnInit() {
    console.log(`ngOnInit ${this.constructor.name} called`)
    let user$ = this.userService.getUser();
    let game$ = this.gameService.getGame(this.game.id);

    // user$.pipe(
    //   combineLatestWith(game$)
    // )
    combineLatest([user$,game$])
      .subscribe(([user, game]) => {
        console.log(`coucou game ${game.id} updated`)
        console.log(game)
        this.user = user;
        this.game = game;
      this.canStartGameAttr = this.canStartGame();
    });

    // user$.subscribe(user => {
    //   // console.log(`coucou user updated`)
    //   this.user = user;
    //   this.canStartGameAttr = this.canStartGame();
    // })
    //
    // game$.subscribe(game => {
    //   console.log(`coucou game ${game.id} updated`)
    //   this.game = game;
    //   this.canStartGameAttr = this.canStartGame();
    // })
  }

  startGame() {
    this.service.start(this.game.id, this.user.id)
      .subscribe(() => {
        }
      )
  }

  private canStartGame(): boolean {
    // TODO empêcher la fonction d'être appelée 36 fois
    console.log(`canStartGame called`)
    return this.isPlayerCreator() && this.isGameJustCreated() && this.playersCountIsValid()
  }

  private playersCountIsValid() {
    return this.game.players.length >= 2 && this.game.players.length <= 6;
  }

  private isPlayerCreator(): boolean {
    return compareUserAndPlayer(this.user, this.game.creator);
  }

  private isGameJustCreated(): boolean {
    return this.game.state === "created"
  }
}
