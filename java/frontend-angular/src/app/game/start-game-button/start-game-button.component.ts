import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgIf} from "@angular/common";
import {GameResponseDto} from "../../openapi-generated";
import {User} from "../../user/user";
import {GameServiceAbstract} from "../game-service-abstract";
import {compareUserAndPlayer} from "../../common/helpers";
import {UserServiceAbstract} from "../../user/user-service.abstract";

@Component({
  selector: 'app-start-game-button',
  standalone: true,
  imports: [
    NgIf
  ],
  template: `
    <button (click)="startGame()" *ngIf="canStartGame()">
      start
    </button>
  `,
  styleUrl: './start-game-button.component.css'
})
export class StartGameButtonComponent {

  @Input() game!: GameResponseDto
  @Output() gameModifiedEvent = new EventEmitter<GameResponseDto>();
  protected user!: User;


  constructor(private service: GameServiceAbstract,
              private userService: UserServiceAbstract) {
    this.user = userService.getUser()
    userService.registerUserUpdatedObserver(this.updateUser)
  }

  startGame() {
    this.service.start(this.game.id, this.user.id)
      .subscribe(response => {
          this.gameModifiedEvent.emit(response);
        }
      )
  }

  private updateUser = (updatedUser: User) => {
    this.user = updatedUser
  }

  canStartGame(): boolean {
    return this.isPlayerCreator() && this.isGameJustCreated() && this.playersCountIsValid()
  }

  private playersCountIsValid() {
    return this.game.players.length >= 2 && this.game.players.length <= 6;
  }

  private isPlayerCreator(): boolean {
    return compareUserAndPlayer(this.user, this.game.creator);
  }

  private isGameJustCreated(): boolean {
    return this.game.state === "created"
  }
}
