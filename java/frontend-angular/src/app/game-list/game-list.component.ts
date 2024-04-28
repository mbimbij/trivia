import {Component} from '@angular/core';
import {CreateGameComponent} from "../create-game/create-game.component";
import {FormsModule} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {GameResponseDto} from "../openapi-generated";
import {GameServiceAbstract} from "../game-service-abstract";
import {RouterLink} from "@angular/router";
import {ObjectAttributePipe} from "../object-attribute.pipe";
import {JoinGameButtonComponent} from "../join-game-button/join-game-button.component";

@Component({
  selector: 'app-game-list',
  standalone: true,
  imports: [
    CreateGameComponent,
    FormsModule,
    NgForOf,
    NgIf,
    ObjectAttributePipe,
    RouterLink,
    JoinGameButtonComponent
  ],
  templateUrl: './game-list.component.html',
  styleUrl: './game-list.component.css'
})
export class GameListComponent {
  title = 'frontend-angular';
  games: GameResponseDto[] = [];
  defaultPlayerName: string = 'player';
  playerName: string;

  constructor(private service: GameServiceAbstract) {
    this.playerName = localStorage.getItem('playerName') || this.defaultPlayerName
  }

  ngOnInit(): void {
    this.service.getGames()
      .subscribe(games => {
        this.games = games;
        return this.games
      });
  }

  addGame(newGame: GameResponseDto) {
    console.log(`app.component.ts received game: ${newGame}`);
    this.games.push(newGame);
  }

  joinGame(game: GameResponseDto) {
    console.log(`joining game ${game.id}`)
    this.service.join(game, {id: 1, name: this.playerName})
      .subscribe(response => {
          this.updateGameWith(response);
        }
      )
  }

  protected updateGameWith(replacement: GameResponseDto) {
    const index = this.games.findIndex(
      game => game.id === replacement.id);
    if (index !== -1) {
      this.games.splice(index, 1, replacement);
    }
  }

  isUserPlayer(game: GameResponseDto): boolean {
    return game.players.some(player => player.name === this.playerName);
  }

  goToGame(game: GameResponseDto) {
    console.log(`going to game ${game.id}`)
  }

  syncNameToLocalStorage() {
    localStorage.setItem('playerName', this.playerName);
  }
}
