import {Component, Input} from '@angular/core';
import {GameResponseDto} from "../../openapi-generated";
import {Router, RouterLink} from "@angular/router";
import {compareUserAndPlayer} from "../../common/helpers";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {User} from "../../user/user";
import {Observable} from "rxjs";
import {Game} from "../game";

@Component({
  selector: 'app-goto-game-button',
  standalone: true,
  imports: [
    RouterLink
  ],
  template: `
<!--     TODO delete state, as the backend is called-->
    <button
      [attr.data-testid]="'goto-button-'+game.id"
      [disabled]="!canGotoGame()"
      (click)="router.navigate(['/games',game.id], { state: game })"
    >
      go to
    </button>
  `,
  styleUrl: './goto-game-button.component.css'
})
export class GotoGameButtonComponent {
  @Input() game!: Game
  private user!: User;
  private user$: Observable<User>;

  constructor(protected router: Router,
              private userService: UserServiceAbstract) {
    this.user$ = userService.getUser();
    this.user$.subscribe(updatedUser => this.user = updatedUser)
  }

  canGotoGame() {
    return this.isPlayer() && this.isGameStarted();
  }

  private isPlayer() {
    return this.game.players.find(player => compareUserAndPlayer(this.user, player)) != null;
  }

  private isGameStarted() {
    return this.game.state === 'started';
  }
}
