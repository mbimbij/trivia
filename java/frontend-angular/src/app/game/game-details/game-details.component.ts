import {Component} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {JoinGameButtonComponent} from "../join-game-button/join-game-button.component";
import {GameResponseDto} from "../../openapi-generated";
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {ObjectAttributePipe} from "../../common/object-attribute.pipe";
import {GotoGameButtonComponent} from "../goto-game-button/goto-game-button.component";
import {StartGameButtonComponent} from "../start-game-button/start-game-button.component";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {Observable} from "rxjs";
import {Game} from "../game";

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [
    JoinGameButtonComponent,
    NgForOf,
    NgIf,
    ObjectAttributePipe,
    GotoGameButtonComponent,
    StartGameButtonComponent,
    AsyncPipe
  ],
  templateUrl: './game-details.component.html',
  styleUrl: './game-details.component.css'
})
export class GameDetailsComponent {
  gameId!: number;
  game!: Game;
  game$!: Observable<Game>;

  constructor(private route: ActivatedRoute,
              private gameService: GameServiceAbstract) {
    this.route.params.subscribe(value => {
      this.gameId = value['id'];
      this.game$ = gameService.getGame(this.gameId);
      this.game$.subscribe(game => this.game=game);
    })
  }
}
