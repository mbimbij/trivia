import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {GameServiceAbstract} from "../game-service-abstract";
import {User} from "../../user/user";

import {UserServiceAbstract} from "../../user/user-service.abstract";

@Component({
  selector: 'app-create-game',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './create-game.component.html',
  styleUrl: './create-game.component.css'
})
export class CreateGameComponent {
  user!: User;

  constructor(private gameService: GameServiceAbstract,
              private userService: UserServiceAbstract) {
  }

  ngOnInit() {
    this.user = this.userService.getUser()
    this.userService.registerUserUpdatedObserver(this.updateUser)
  }

  createGame(newGameName: string) {
    this.gameService.create(newGameName, this.user)
      .subscribe(() => {
      })
  }

  private updateUser = (updatedUser: User) => {
    this.user = updatedUser
  }
}
