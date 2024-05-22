import {Component} from '@angular/core';
import {GameLog, GameResponseDto, UserDto} from "../../openapi-generated";
import {ActivatedRoute, Router} from "@angular/router";
import {NgForOf, NgIf} from '@angular/common';
import {UserService} from "../../user/user.service";
import {comparePlayers, userToPlayerDto} from "../../common/helpers";
import {GameService} from "../game.service";
import {User} from "../../user/user";
import {Player} from "../../user/player";

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './game.component.html',
  styleUrl: './game.component.css'
})
export class GameComponent {
  protected player!: Player;
  private gameId!: number;
  protected game!: GameResponseDto;
  protected logs: Array<string> = [...Array(20).keys()].map(value => `message: ${value}`);
  protected dataLoaded: boolean = false;

  constructor(private route: ActivatedRoute,
              protected router: Router,
              private userService: UserService,
              private gameService: GameService) {
  }

  private ngOnInit() {
    this.player = userToPlayerDto(this.userService.getUser())
    this.userService.registerUserUpdatedObserver(this.updateUser)
    this.route.params.subscribe(value => {
      this.gameId = value['id'];
    })

    this.gameService.getGame(this.gameId)
      .subscribe(value => {
        this.game = value
        this.dataLoaded = true
        this.setCoinCount();
      })
    this.gameService.registerGameUpdatedObserver(this.gameId, this.updateGameWith);

    this.gameService.getGameLogs(this.gameId)
      .subscribe(gameLogs => {
        this.logs = gameLogs.map(log => log.value)
      })
    this.gameService.registerGameLogsObserver(this.gameId, this.addLogs);
  }

  private setCoinCount() {
    // TODO améliorer update player.coinCount -> introduire une update via websocket par exemple
    const index = this.game.players.findIndex(
      player => player.id === this.player.id);
    if (index !== -1) {
      this.player.coinCount = this.game.players[index].coinCount;
    }
  }

  protected canPlayTurn() {
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
    if (this.dataLoaded) {
      this.scrollLogsToBottom()
    }
  }

  private scrollLogsToBottom() {
    let element = document.getElementById("messagesContainer")!;
    element.scrollTop = element.scrollHeight
  }

  private updateUser = (updatedUser: User) => {
    const index = this.game.players.findIndex(
      player => player.id === this.player.id);
    if (index !== -1) {
      this.player = this.game.players[index];
    }
  }

  private updateGameWith = (updatedGame: GameResponseDto) => {
    this.game = updatedGame
  }

  private addLogs = (gameLog: GameLog) => {
    this.logs.push(gameLog.value);
  }

  protected playerWon(): boolean {
    return comparePlayers(this.player, this.game.winner)
  }
}
