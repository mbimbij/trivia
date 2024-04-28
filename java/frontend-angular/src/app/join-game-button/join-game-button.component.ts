import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgIf} from "@angular/common";
import {GameResponseDto} from "../openapi-generated";
import {GameServiceAbstract} from "../game-service-abstract";

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

  constructor(private service: GameServiceAbstract) {
    this.playerName = localStorage.getItem('playerName')!
  }

  isUserPlayer(): boolean {
    return this.game.players.some(player => player.name === this.playerName);
  }

  joinGame() {
    console.log(`plop ${this.playerName} joining game ${this.game.id}`)
    this.service.join(this.game, {id: 1, name: this.playerName})
      .subscribe(response => {
          this.gameModifiedEvent.emit(response);
        }
      )
  }
}
