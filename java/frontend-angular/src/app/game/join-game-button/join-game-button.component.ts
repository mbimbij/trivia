import {Component, Input} from '@angular/core';
import {NgIf} from "@angular/common";
import {GameResponseDto} from "../../openapi-generated";
import {GameServiceAbstract} from "../game-service-abstract";
import {User} from "../../user/user";
import {UserServiceAbstract} from "../../user/user-service.abstract";

@Component({
  selector: 'app-join-game-button',
  standalone: true,
  imports: [
    NgIf
  ],
  template: `
    @if (canJoin()) {
      <button (click)="joinGame()">
        join
      </button>
    } @else if (isGameStarted()) {
      <span *ngIf="!canJoin()">{{ 'game started' }}</span>
    } @else if (isPlayerInGame()) {
      <span *ngIf="!canJoin()">{{ 'already joined' }}</span>
    } @else {
      <span *ngIf="!canJoin()">{{ 'cannot join' }}</span>
    }
  `,
  styleUrl: './join-game-button.component.css'
})
export class JoinGameButtonComponent {

  @Input() game!: GameResponseDto
  protected user!: User;

  constructor(private gameService: GameServiceAbstract,
              private userService: UserServiceAbstract) {
    this.user = userService.getUser()
    userService.registerUserUpdatedObserver(this.updateUser)
  }

  canJoin(): boolean {
    return !this.isPlayerInGame() && this.playersCountsLessThanMax() && !this.isGameStarted()
  }

  protected isPlayerInGame(): boolean {
    return this.game.players.some(player => player.name === this.user.name);
  }

  private playersCountsLessThanMax() {
    return this.game.players.length < 6;
  }

  protected isGameStarted(): boolean {
    return this.game.state === 'started';
  }


  joinGame() {
    this.gameService.join(this.game, this.user)
      .subscribe(() => {
      })
  }

  private updateUser = (updatedUser: User) => {
    this.user = updatedUser
  }
}
