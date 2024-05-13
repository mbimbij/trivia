import {Component} from '@angular/core';
import {GameLog, GameResponseDto, UserDto} from "../openapi-generated";
import {ActivatedRoute} from "@angular/router";
import {NgForOf, NgIf} from '@angular/common';
import {LocalStorageService} from "../local-storage.service";
import {compareUserDto} from "../helpers";
import {GameService} from "../game.service";

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
  protected user!: UserDto;
  private gameId!: number;
  protected game!: GameResponseDto;
  protected logs: Array<string> = [...Array(20).keys()].map(value => `message: ${value}`);
  protected dataLoaded: boolean = false;

  constructor(private route: ActivatedRoute,
              private localStorageService: LocalStorageService,
              private gameService: GameService) {
  }

  protected isCurrentPlayer() {
    return compareUserDto(this.user, this.game.currentPlayer)
  }

  protected playTurn() {
    this.gameService.playTurn(this.gameId, this.user.id).subscribe(value => {
      this.game = value
    });
  }

  private ngOnInit() {
    this.user = this.localStorageService.getUser()
    this.localStorageService.registerUserUpdatedObserver(this.updateUser)
    this.route.params.subscribe(value => {
      this.gameId = value['id'];
    })

    this.gameService.getGame(this.gameId)
      .subscribe(value => {
        this.game = value
        this.dataLoaded = true
      })
    this.gameService.registerGameUpdatedObserver(this.gameId, this.updateGameWith);

    this.gameService.getGameLogs(this.gameId)
      .subscribe(gameLogs => {
        this.logs = gameLogs.map(log => log.value)
      })
    this.gameService.registerGameLogsObserver(this.gameId, this.addLogs);
  }

  private ngAfterViewChecked() {
    if(this.dataLoaded){
      this.scrollLogsToBottom()
    }
  }

  private scrollLogsToBottom() {
    let element = document.getElementById("messagesContainer")!;
    element.scrollTop = element.scrollHeight
  }

  private updateUser = (updatedUser: UserDto) => {
    this.user = updatedUser
  }

  private updateGameWith = (updatedGame: GameResponseDto) => {
    this.game = updatedGame
  }

  private addLogs = (gameLog: GameLog) => {
    this.logs.push(gameLog.value);
  }
}
