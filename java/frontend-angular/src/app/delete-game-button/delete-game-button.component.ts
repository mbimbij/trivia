import {Component, Input} from '@angular/core';
import {GameServiceAbstract} from "../game-service-abstract";
import {UserService} from "../user.service";
import {GameResponseDto} from "../openapi-generated";
import {compareUserAndPlayer} from "../helpers";
import {User} from "../user";

@Component({
  selector: 'app-delete-game-button',
  standalone: true,
  imports: [],
  template: `
    <button [disabled]="!canDeleteGame()" (click)="deleteGame()">
      delete
    </button>
  `,
  styleUrl: './delete-game-button.component.css'
})
export class DeleteGameButtonComponent {
  @Input() gameId!: number;
  @Input() game!: GameResponseDto;
  private user: User;

  constructor(private gameService: GameServiceAbstract,
              private localStorageService: UserService) {
    this.user = localStorageService.getUser()
    localStorageService.registerUserUpdatedObserver(this.updateUser)
  }

  private updateUser = (updatedUser: User) => {
    this.user = updatedUser
  }

  protected canDeleteGame() {
    return compareUserAndPlayer(this.user, this.game.creator)
  }

  protected deleteGame() {
    this.gameService.delete(this.gameId).subscribe(() => {});
  }
}
