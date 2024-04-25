import {Component} from '@angular/core';
import {GameServiceAbstract} from "./game-service-abstract";
import {Game} from "./game";
import {GameResponseDto} from "./openapi-generated";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend-angular';
  games: GameResponseDto[] = [];
  playerName: string = 'player';

  constructor(private service: GameServiceAbstract) {
  }

  ngOnInit(): void {
    console.log("trivia application up and running")
    this.service.getGames()
      .subscribe(games => {
        this.games = games;
        return this.games
      });
  }

  addGame(newGame: GameResponseDto) {
    console.log("app.component.ts received game: "+newGame);
    this.games.push(newGame);
  }
}
