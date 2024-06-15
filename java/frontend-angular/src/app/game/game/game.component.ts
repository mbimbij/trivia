import {Component} from '@angular/core';
import {GameLog} from "../../openapi-generated";
import {ActivatedRoute, Router} from "@angular/router";
import {AsyncPipe, NgForOf, NgIf} from '@angular/common';
import {comparePlayers, generateRandomString, userToPlayer, userToPlayerDto} from "../../common/helpers";
import {GameService} from "../game.service";
import {Player} from "../../user/player";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {Observable} from "rxjs";
import {Game} from "../game";
import {ConsoleLogPipe} from "../../console-log.pipe";
import {GameServiceAbstract} from "../../services/game-service-abstract";

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    AsyncPipe,
    ConsoleLogPipe
  ],
  templateUrl: './game.component.html',
  styleUrl: './game.component.css'
})
export class GameComponent {
  private readonly id: string;
  protected player!: Player;
  private gameId!: number;
  protected game!: Game;
  protected game$!: Observable<Game>
  protected logs: Array<string> = [];
  protected gameLogs$!: Observable<GameLog[]>;

  constructor(private route: ActivatedRoute,
              protected router: Router,
              private userService: UserServiceAbstract,
              private gameService: GameServiceAbstract) {
    this.id = `${this.constructor.name} - ${generateRandomString(4)}`;
    console.log(`constructor ${this.id} called`)
    this.route.params.subscribe(value => {
      this.gameId = Number.parseInt(value['id']);
      this.game$ = gameService.getGame(this.gameId);
      this.game$.subscribe(game => {
        this.game=game;
      })
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

  ngOnInit() {
    this.gameService.initGameLogs(this.gameId);
    this.gameLogs$ = this.gameService.getGameLogs(this.gameId);

    // TODO améliorer l'initialisation des subjects et observables en tenant compte des dépendances
    this.userService.getUser().subscribe(updatedUser => this.player = userToPlayer(updatedUser))

    //
    // this.gameService.getGameLogs(this.gameId)
    //   .subscribe(gameLogs => {
    //     this.logs = gameLogs.map(log => log.value)
    //   })
  }

  protected canPlayTurn() {
    // TODO empêcher la fonction d'être appelée 36 fois
    // console.log(`canPlayTurn called`)
    return this.isCurrentPlayer() && !this.isGameEnded;
  }

  get isGameEnded(): boolean {
    return this.game.state === "ended";
  }

  protected isCurrentPlayer() {
    return comparePlayers(this.player, this.game.currentPlayer)
  }

  protected playTurn() {
    this.gameService.playTurn(this.gameId, this.player.id).subscribe(value => {
      this.game = value
      this.setCoinCount();
    });
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

  private addLogs = (gameLog: GameLog) => {
    this.logs.push(gameLog.value);
  }

  protected playerWon(): boolean {
    return comparePlayers(this.player, this.game.winner)
  }
}
