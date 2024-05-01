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
  templateUrl: './join-game-button.component.html',
  styleUrl: './join-game-button.component.css'
})
export class JoinGameButtonComponent {

  @Input() game!: GameResponseDto
  @Input() defaultMessage: string = 'already joined'
  @Output() gameModifiedEvent = new EventEmitter<GameResponseDto>();
  private playerName: string

  constructor(private service: GameServiceAbstract,
              private localStorageService: LocalStorageService) {
    this.playerName = localStorage.getItem('playerName')!
    localStorageService.registerObserver(this.updateName)
  }

  isUserPlayer(): boolean {
    return this.game.players.some(player => player.name === this.playerName);
  }

  joinGame() {
    this.service.join(this.game, {id: 1, name: this.playerName})
      .subscribe(response => {
          this.gameModifiedEvent.emit(response);
        }
      )
  }

  private updateName = (newName: string) => {
    this.playerName = newName
  }
}
