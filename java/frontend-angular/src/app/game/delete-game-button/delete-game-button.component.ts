import {Component, Input} from '@angular/core';
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {GameResponseDto} from "../../openapi-generated";
import {compareUserAndPlayer} from "../../common/helpers";
import {User} from "../../user/user";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {Observable} from "rxjs";
import {Game} from "../game";

@Component({
  selector: 'app-delete-game-button',
  standalone: true,
  imports: [],
  template: `
    <button [attr.data-testid]="'delete-button-'+game.id" [disabled]="!canDeleteGame()" (click)="deleteGame()">
      delete
    </button>
  `,
  styleUrl: './delete-game-button.component.css'
})
export class DeleteGameButtonComponent {
  @Input() game!: Game;
  private user!: User;
  private user$: Observable<User>;

  constructor(private gameService: GameServiceAbstract,
              private userService: UserServiceAbstract) {
    this.user$ = userService.getUser();
    this.user$.subscribe(updatedUser => this.user = updatedUser)
  }
  protected canDeleteGame() {
    // TODO empêcher la fonction d'être appelée 36 fois
    // console.log(`canDeleteGame called`)
    return compareUserAndPlayer(this.user, this.game.creator)
  }

  protected deleteGame() {
    this.gameService.delete(this.game.id).subscribe(() => {});
  }
}
