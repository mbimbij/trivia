import {Component} from '@angular/core';
import {CreateGameComponent} from "../create-game/create-game.component";
import {FormsModule} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {GameResponseDto} from "../openapi-generated";
import {GameServiceAbstract} from "../game-service-abstract";
import {RouterLink} from "@angular/router";
import {ObjectAttributePipe} from "../object-attribute.pipe";
import {JoinGameButtonComponent} from "../join-game-button/join-game-button.component";
import {LocalStorageService} from "../local-storage.service";
import {GotoGameButtonComponent} from "../goto-game-button/goto-game-button.component";
import {StartGameButtonComponent} from "../start-game-button/start-game-button.component";

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
    JoinGameButtonComponent,
    GotoGameButtonComponent,
    StartGameButtonComponent
  ],
  templateUrl: './game-list.component.html',
  styleUrl: './game-list.component.css'
})
export class GameListComponent {
  title = 'frontend-angular';
  games: GameResponseDto[] = [];
  defaultPlayerName: string = 'player';
  playerName: string;

  constructor(private service: GameServiceAbstract,
              private localStorageService: LocalStorageService) {
    this.playerName = localStorage.getItem('playerName') ?? this.defaultPlayerName
  }

  ngOnInit(): void {
    this.service.getGames()
      .subscribe(games => {
        this.games = games;
        return this.games
      });
  }

  addGame(newGame: GameResponseDto) {
    console.log(`game created: ${JSON.stringify(newGame)}`);
    this.games.push(newGame);
  }
  protected updateGameWith(replacement: GameResponseDto) {
    const index = this.games.findIndex(
      game => game.id === replacement.id);
    if (index !== -1) {
      this.games.splice(index, 1, replacement);
    }
  }
  syncNameToLocalStorage() {
    this.localStorageService.updatePlayerName(this.playerName)
  }
}
