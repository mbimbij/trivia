import {Component, Input} from '@angular/core';
import {GameServiceAbstract} from "../game-service-abstract";
import {GameResponseDto} from "../../openapi-generated";
import {compareUserAndPlayer} from "../../common/helpers";
import {User} from "../../user/user";
import {UserServiceAbstract} from "../../user/user-service.abstract";

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
              private localStorageService: UserServiceAbstract) {
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
