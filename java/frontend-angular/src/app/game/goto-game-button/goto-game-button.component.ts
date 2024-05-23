import {Component, Input} from '@angular/core';
import {GameResponseDto} from "../../openapi-generated";
import {Router, RouterLink} from "@angular/router";
import {compareUserAndPlayer} from "../../common/helpers";
import {UserServiceAbstract} from "../../user/user-service.abstract";
import {User} from "../../user/user";

@Component({
  selector: 'app-goto-game-button',
  standalone: true,
  imports: [
    RouterLink
  ],
  template: `
    <button [disabled]="!canGotoGame()" (click)="router.navigate(['/games',game.id], { state: game })">
      go to
    </button>
  `,
  styleUrl: './goto-game-button.component.css'
})
export class GotoGameButtonComponent {
  @Input() game!: GameResponseDto
  private user: User;

  constructor(protected router: Router,
              private userService: UserServiceAbstract) {
    this.user = userService.getUser();
    userService.registerUserUpdatedObserver(this.updateUser)
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

  private updateUser = (updatedUser: User) => {
    this.user = updatedUser
  }
}
