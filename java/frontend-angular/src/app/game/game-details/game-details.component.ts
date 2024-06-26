import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {JoinGameButtonComponent} from "../join-game-button/join-game-button.component";
import {AsyncPipe, NgClass, NgForOf, NgIf} from "@angular/common";
import {ObjectAttributePipe} from "../../common/object-attribute.pipe";
import {GotoGameButtonComponent} from "../goto-game-button/goto-game-button.component";
import {StartGameButtonComponent} from "../start-game-button/start-game-button.component";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {Observable} from "rxjs";
import {Game} from "../game";
import {generateRandomString} from "../../common/helpers";
import {ConsoleLogPipe} from "../../console-log.pipe";

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
    AsyncPipe,
    NgClass,
    ConsoleLogPipe
  ],
  templateUrl: './game-details.component.html',
  styleUrl: './game-details.component.css'
})
export class GameDetailsComponent {
  gameId!: number;
  game$!: Observable<Game>;
  private readonly id: string;

  constructor(private route: ActivatedRoute,
              protected router: Router,
              private gameService: GameServiceAbstract) {
    this.id = `${this.constructor.name} - ${generateRandomString(4)}`;
    console.log(`constructor ${this.id} called`)
    this.route.params.subscribe(value => {
      this.gameId = Number.parseInt(value['id']);
      this.game$ = gameService.getGame(this.gameId);
    })
  }

  ngOnInit(): void {
    console.log(`ngOnInit ${this.constructor.name} - ${this.id} called`)
  }

  ngOnDestroy(): void {
    console.log(`ngOnDestroy ${this.constructor.name} - ${this.id} called`)
  }

}
