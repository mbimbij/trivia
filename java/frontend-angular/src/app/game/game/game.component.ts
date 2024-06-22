import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {GameLog} from "../../openapi-generated";
import {ActivatedRoute, Router} from "@angular/router";
import {AsyncPipe, NgForOf, NgIf} from '@angular/common';
import {comparePlayers, generateRandomString, userToPlayer} from "../../common/helpers";
import {Player} from "../../user/player";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {Observable, of} from "rxjs";
import {Game} from "../game";
import {ConsoleLogPipe} from "../../console-log.pipe";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {RollDiceComponent} from "./roll-dice/roll-dice.component";
import {AnswerQuestionComponent} from "./answer-question/answer-question.component";

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    AsyncPipe,
    ConsoleLogPipe,
    RollDiceComponent,
    AnswerQuestionComponent
  ],
  templateUrl: './game.component.html',
  styleUrl: './game.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GameComponent {
  private readonly id: string;
  @Input("player") protected player!: Player;
  private gameId!: number;
  @Input("game") private game!: Game;
  protected game$!: Observable<Game>
  protected gameLogs$!: Observable<GameLog[]>;

  constructor(private route: ActivatedRoute,
              protected router: Router,
              private userService: UserServiceAbstract,
              private gameService: GameServiceAbstract) {
    this.id = `${this.constructor.name} - ${generateRandomString(4)}`;
    console.log(`constructor ${this.id} called`)
    this.route.params.subscribe(value => {
      this.gameId = Number.parseInt(value['id']);
      this.game$ = this.gameService.getGame(this.gameId);
      this.game$.subscribe(game => {
        this.game = game;
      })
    })
  }

  ngOnInit() {
    this.gameService.initGameLogs(this.gameId);
    this.gameLogs$ = this.gameService.getGameLogs(this.gameId);

    // TODO améliorer l'initialisation des subjects et observables en tenant compte des dépendances
    this.userService.getUser().subscribe(updatedUser => {
      return this.player = userToPlayer(updatedUser);
    })
  }

  private setCoinCount() {
    // TODO améliorer update player.coinCount -> introduire une update via websocket par exemple
    const index = this.game.players.findIndex(
      player => player.id === this.player.id);
    if (index !== -1) {
      this.player.coinCount = this.game.players[index].coinCount;
    }
  }

  get isGameEnded(): boolean {
    return this.game.state === "ended";
  }

  protected isCurrentPlayer() {
    return comparePlayers(this.player, this.game.currentPlayer)
  }
  private ngAfterViewChecked() {
    this.scrollLogsToBottom()
  }

  private scrollLogsToBottom() {
    let element = document.getElementById("messagesContainer")!;
    if (element) {
      element.scrollTop = element.scrollHeight
    }
  }

  protected playerWon(): boolean {
    return comparePlayers(this.player, this.game.winner)
  }

  /**
   * For tests only
   * @param player
   */
  setPlayer(player: Player) {
    this.player = player
  }

  /**
   * For tests only
   * @param game
   */
  setGame(game: Game) {
    this.game$ = of(game)
  }
}
