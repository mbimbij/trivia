import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {Nobody, User} from "../../user/user";

import {UserServiceAbstract} from "../../services/user-service.abstract";
import {Observable} from "rxjs";

@Component({
  selector: 'app-create-game',
  standalone: true,
  imports: [
    FormsModule
  ],
  template: `
    <label for="newGameName">Create Game</label>
    <input type="text" id="newGameName" required minlength="1" maxlength="100" size="20" #newGameName/>
    <button (click)="createGame(newGameName.value)">create</button>
  `,
  styleUrl: './create-game.component.css'
})
export class CreateGameComponent {
  private user: User = Nobody.instance;
  private user$: Observable<User>;

  constructor(private gameService: GameServiceAbstract,
              private userService: UserServiceAbstract) {
    this.user$ = userService.getUser();
    this.user$.subscribe(updatedUser => this.user = updatedUser)
  }

  protected createGame(newGameName: string) {
    this.gameService.create(newGameName, this.user)
      .subscribe(() => {
      })
  }
}
