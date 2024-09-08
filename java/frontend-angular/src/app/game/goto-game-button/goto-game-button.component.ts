import {ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnDestroy, SimpleChanges} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {compareUserAndPlayer} from "../../common/helpers";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {User} from "../../user/user";
import {BehaviorSubject, Subscription} from "rxjs";
import {Game} from "../game";
import {Identifiable} from "../../common/identifiable";
import {State} from "../../openapi-generated/game";
import {AsyncPipe, NgIf} from "@angular/common";

@Component({
  selector: 'app-goto-game-button',
  standalone: true,
  imports: [
    RouterLink,
    AsyncPipe,
    NgIf
  ],
  template: `
    <!--     TODO delete state, as the backend is called-->
    <button
      [attr.data-testid]="'goto-button-'+game.id"
      [disabled]="!(canGotoGame$ | async)"
      (click)="router.navigate(['/games',game.id], { state: game })"
    >
      go to
    </button>
    {{ checkRender() }}
  `,
  styleUrl: './goto-game-button.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GotoGameButtonComponent extends Identifiable implements OnDestroy {
  @Input() game!: Game
  private user!: User
  protected canGotoGame$ = new BehaviorSubject<boolean>(false);
  private subscription?: Subscription;

  constructor(protected router: Router,
              private userService: UserServiceAbstract) {
    super()
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['game']){
      this.game = changes['game'].currentValue;
      this.updateButtonVisibility();
    }
  }

  ngOnInit(){
    this.subscription = this.userService.getUser().subscribe(updatedUser => {
      this.user = updatedUser;
      this.updateButtonVisibility();
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  private updateButtonVisibility() {
    this.canGotoGame$.next(this.canGotoGame(this.user))
  }

  private canGotoGame(user: User) {
    return this.isPlayer(user) && this.isGameStarted();
  }

  private isPlayer(user: User) {
    return this.game.players.find(player => compareUserAndPlayer(user, player)) != null;
  }

  private isGameStarted() {
    return this.game.state === State.Started;
  }
}
