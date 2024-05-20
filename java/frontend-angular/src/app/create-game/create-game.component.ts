import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {GameResponseDto, TriviaControllerService} from "../openapi-generated";
import {FormsModule} from "@angular/forms";
import {tap} from "rxjs";
import {GameServiceAbstract} from "../game-service-abstract";
import {User} from "../user";
import {UserService} from "../user.service";

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
              private userService: UserService) {
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
