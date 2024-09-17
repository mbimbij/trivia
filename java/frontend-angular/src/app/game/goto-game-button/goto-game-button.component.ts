import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  OnChanges,
  OnDestroy,
  SimpleChanges
} from '@angular/core';
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
export class GotoGameButtonComponent extends Identifiable implements OnChanges{
  @Input() game!: Game
  @Input() userId!: string
  protected canGotoGame$ = new BehaviorSubject<boolean>(false);

  constructor(protected router: Router) {
    super()
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['game']){
      this.game = changes['game'].currentValue;
    }
    if(changes['userId']){
      this.userId = changes['userId'].currentValue;
    }
    this.updateButtonVisibility();
  }

  private updateButtonVisibility() {
    this.canGotoGame$.next(this.canGotoGame())
  }

  private canGotoGame() {
    return this.isPlayer() && this.isGameStarted();
  }

  private isPlayer() {
    return this.game.players.find(player => this.userId === player?.id) != null;
  }

  private isGameStarted() {
    return this.game.state === State.Started;
  }
}
