import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {NgIf} from "@angular/common";
import {GameResponseDto} from "../openapi-generated";
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
  @Output() gameModifiedEvent = new EventEmitter<GameResponseDto>();
  cannotJoinReason: string = 'already joined'
  private playerName: string

  constructor(private service: GameServiceAbstract,
              private localStorageService: LocalStorageService) {
    this.playerName = localStorage.getItem('playerName')!
    localStorageService.registerObserver(this.updateName)
  }

  canJoin(): boolean {
    return !this.isPlayerInGame() && this.playersCountsLessThanMax()
  }

  joinGame() {
    this.service.join(this.game, {id: 1, name: this.playerName})
      .subscribe(response => {
          this.gameModifiedEvent.emit(response);
        }
      )
  }

  private playersCountsLessThanMax() {
    return this.game.players.length < 6;
  }

  private isPlayerInGame(): boolean {
    return this.game.players.some(player => player.name === this.playerName);
  }

  private updateName = (newName: string) => {
    this.playerName = newName
  }
}
