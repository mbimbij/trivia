import {Component, Input} from '@angular/core';
import {GameServiceAbstract} from "../game-service-abstract";
import {LocalStorageService} from "../local-storage.service";
import {GameResponseDto, UserDto} from "../openapi-generated";
import {compareUserDto} from "../helpers";

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
  private user: UserDto;

  constructor(private gameService: GameServiceAbstract,
              private localStorageService: LocalStorageService) {
    this.user = localStorageService.getUser()
    localStorageService.registerUserUpdatedObserver(this.updateUser)
  }

  private updateUser = (updatedUser: UserDto) => {
    this.user = updatedUser
  }

  protected canDeleteGame() {
    return compareUserDto(this.user, this.game.creator)
  }

  protected deleteGame() {
    this.gameService.delete(this.gameId).subscribe(() => {});
  }
}
