import {Component, OnDestroy} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {Nobody, User} from "../../user/user";

import {UserServiceAbstract} from "../../services/user-service.abstract";
import {Observable, Subscription} from "rxjs";
import {Identifiable} from "../../common/identifiable";

@Component({
  selector: 'app-create-game',
  standalone: true,
  imports: [
    FormsModule
  ],
  template: `
    <label for="newGameName">Create Game</label>
    <input
      [attr.data-testid]="'create-game-name'"
      #newGameName
      type="text" id="newGameName"
      required minlength="1" maxlength="100" size="20"
      (keyup.enter)="createGame(newGameName.value)"
    />
    <button
      [attr.data-testid]="'create-game-validate'"
      (click)="createGame(newGameName.value)">create</button>
  `,
  styleUrl: './create-game.component.css'
})
export class CreateGameComponent extends Identifiable implements OnDestroy{
  private user: User = Nobody.instance;
  private user$: Observable<User>;
  private userSubscription: Subscription;
  private newGameSubscription?: Subscription;

  constructor(private gameService: GameServiceAbstract,
              private userService: UserServiceAbstract) {
    super()
    this.user$ = userService.getUser();
    this.userSubscription = this.user$.subscribe(updatedUser => this.user = updatedUser);
  }

  ngOnDestroy(): void {
        this.userSubscription.unsubscribe();
        this.newGameSubscription?.unsubscribe();
    }

  protected createGame(newGameName: string) {
    this.newGameSubscription = this.gameService.create(newGameName, this.user)
      .subscribe(newGame => {
        console.log(`created game: ${newGame.id}`)
      });
  }
}
