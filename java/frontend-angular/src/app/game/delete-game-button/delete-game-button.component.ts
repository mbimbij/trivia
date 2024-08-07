import {Component, Input, OnDestroy} from '@angular/core';
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {compareUserAndPlayer} from "../../common/helpers";
import {User} from "../../user/user";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {Observable, Subscription} from "rxjs";
import {Game} from "../game";
import {Identifiable} from "../../common/identifiable";

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
export class DeleteGameButtonComponent extends Identifiable implements OnDestroy {
  @Input() game!: Game;
  private user!: User;
  private user$: Observable<User>;
  private userSubscription: Subscription;
  private gameSubscription?: Subscription;

  constructor(private gameService: GameServiceAbstract,
              private userService: UserServiceAbstract) {
    super()
    this.user$ = userService.getUser();
    this.userSubscription = this.user$.subscribe(updatedUser => this.user = updatedUser);
  }

  ngOnDestroy(): void {
        this.userSubscription.unsubscribe();
        this.gameSubscription?.unsubscribe();
    }
  protected canDeleteGame() {
    // TODO empêcher la fonction d'être appelée 36 fois
    // console.log(`canDeleteGame called`)
    return compareUserAndPlayer(this.user, this.game.creator)
  }

  protected deleteGame() {
    this.gameSubscription = this.gameService.delete(this.game.id).subscribe(() => {});
  }
}
