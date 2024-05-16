import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {NgIf} from "@angular/common";
import {GameResponseDto, UserDto} from "../openapi-generated";
import {GameServiceAbstract} from "../game-service-abstract";
import {LocalStorageService} from "../local-storage.service";

@Component({
  selector: 'app-join-game-button',
  standalone: true,
  imports: [
    NgIf
  ],
  template: `
    <button (click)="joinGame()" *ngIf="canJoin()">
      join
    </button>
    <span *ngIf="!canJoin()">{{ cannotJoinReason }}</span>
  `,
  styleUrl: './join-game-button.component.css'
})
export class JoinGameButtonComponent {

  @Input() game!: GameResponseDto
  cannotJoinReason: string = 'already joined'
  protected user!: UserDto;

  constructor(private gameService: GameServiceAbstract,
              private localStorageService: LocalStorageService) {
    this.user = localStorageService.getUser()
    localStorageService.registerUserUpdatedObserver(this.updateUser)
  }

  canJoin(): boolean {
    return !this.isPlayerInGame() && this.playersCountsLessThanMax()
  }

  joinGame() {
    this.gameService.join(this.game, this.user)
      .subscribe(() => {})
  }

  private playersCountsLessThanMax() {
    return this.game.players.length < 6;
  }

  private isPlayerInGame(): boolean {
    return this.game.players.some(player => player.name === this.user.name);
  }

  private updateUser = (updatedUser: UserDto) => {
    this.user = updatedUser
  }
}
