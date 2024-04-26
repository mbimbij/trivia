import {ChangeDetectorRef, Component} from '@angular/core';
import {CreateGameComponent} from "../create-game/create-game.component";
import {FormsModule} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {GameResponseDto} from "../openapi-generated";
import {GameServiceAbstract} from "../game-service-abstract";
import {Router} from "@angular/router";
import {ObjectAttributePipe} from "../object-attribute.pipe";

@Component({
  selector: 'app-game-list',
  standalone: true,
  imports: [
    CreateGameComponent,
    FormsModule,
    NgForOf,
    NgIf,
    ObjectAttributePipe
  ],
  templateUrl: './game-list.component.html',
  styleUrl: './game-list.component.css'
})
export class GameListComponent {
  title = 'frontend-angular';
  games: GameResponseDto[] = [];
  playerName: string = 'player4';

  constructor(private service: GameServiceAbstract,
              private router: Router,
              private cdr: ChangeDetectorRef) {
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

  private updateGameWith(replacement: GameResponseDto) {
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
}
