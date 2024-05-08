import {Component, EventEmitter, Input, Output} from '@angular/core';
import {GameResponseDto, UserDto} from "../openapi-generated";
import {GameServiceAbstract} from "../game-service-abstract";
import {LocalStorageService} from "../local-storage.service";
import {NgIf} from "@angular/common";
import {compareUserDto} from "../helpers";

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
  protected user!: UserDto;


  constructor(private service: GameServiceAbstract,
              private localStorageService: LocalStorageService) {
    this.user = localStorageService.getUser()
    localStorageService.registerUserUpdatedObserver(this.updateUser)
  }

  startGame() {
    this.service.start(this.game.id,1)
      .subscribe(response => {
          this.gameModifiedEvent.emit(response);
        }
      )
  }

  private updateUser = (updatedUser: UserDto) => {
    console.log(`${this.constructor.name} - update user ${this.user}`)
    this.user = updatedUser
  }

  canStartGame(): boolean{
    return this.isPlayerCreator() && !this.isGameStarted() && this.playersCountIsValid()
  }

  private playersCountIsValid() {
    return this.game.players.length >= 2 && this.game.players.length <= 6;
  }

  private isPlayerCreator(): boolean {
    return compareUserDto(this.game.creator, this.user);
  }

  private isGameStarted(): boolean {
    return this.game.state === "started"
  }
}
