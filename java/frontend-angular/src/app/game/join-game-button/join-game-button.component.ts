import {Component, Input} from '@angular/core';
import {GameResponseDto} from "../../openapi-generated";
import {GameServiceAbstract} from "../game-service-abstract";
import {Nobody, User} from "../../user/user";
import {UserServiceAbstract} from "../../user/user-service.abstract";
import {Observable} from "rxjs";

@Component({
  selector: 'app-join-game-button',
  standalone: true,
  template: `
    @if (canJoin()) {
      <button (click)="joinGame()">
        join
      </button>
    } @else if (isGameStarted()) {
      <span>{{ 'game started' }}</span>
    } @else if (isPlayerInGame()) {
      <span>{{ 'already joined' }}</span>
    } @else {
      <span>{{ 'cannot join' }}</span>
    }
  `,
  styleUrl: './join-game-button.component.css'
})
export class JoinGameButtonComponent {

  @Input() game!: GameResponseDto
  protected user: User = Nobody.instance;
  user$: Observable<User>;

  constructor(private gameService: GameServiceAbstract,
              private userService: UserServiceAbstract) {
    this.user$ = userService.getUser();
    this.user$.subscribe(updatedUser => this.user = updatedUser)
  }

  canJoin(): boolean {
    // TODO empêcher cette fonction d'être appelée 36 fois
    console.log(`canJoin called`)
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
  //
  // private updateUser = (updatedUser: User) => {
  //   this.user = updatedUser
  // }
}
