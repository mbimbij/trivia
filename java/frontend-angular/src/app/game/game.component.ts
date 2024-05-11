import {Component} from '@angular/core';
import {GameResponseDto, UserDto} from "../openapi-generated";
import {ActivatedRoute} from "@angular/router";
import {NgForOf} from '@angular/common';
import {LocalStorageService} from "../local-storage.service";
import {compareUserDto} from "../helpers";
import {RxStompService} from "../rx-stomp.service";
import {IMessage} from "@stomp/rx-stomp";
import {GameService} from "../game.service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './game.component.html',
  styleUrl: './game.component.css'
})
export class GameComponent {
  protected user!: UserDto;
  private gameId!: number;
  protected game!: GameResponseDto;
  protected logs: Array<string> = [...Array(20).keys()].map(value => `message: ${value}`);
  private counter: number = 0;

  constructor(private route: ActivatedRoute,
              private localStorageService: LocalStorageService,
              private rxStompService: RxStompService,
              private gameService: GameService,
              private httpClient: HttpClient) {
    this.user = localStorageService.getUser()
    console.log(`${this.constructor.name} - create with user ${this.user}`)
    this.route.params.subscribe(value => {
      this.gameId = value['id'];
    })
    localStorageService.registerUserUpdatedObserver(this.updateUser)
  }

  protected isCurrentPlayer() {
    return compareUserDto(this.user, this.game.currentPlayer)
  }

  protected newMessage() {
    this.logs.push(`newMessage ${this.counter++}`)
  }

  protected playTurn() {
    this.gameService.playTurn(this.gameId, this.user.id).subscribe(value => {
      this.game = value
    });
  }

  private ngOnInit() {
    // TODO récupérer le jeu depuis appel au backend, et update de l'état de la partie via websocket quand d'autres joueurs jouent sur une autre tab / un autre navigateur
    this.game = history?.state
    this.rxStompService.watch(`/topic/game/${this.gameId}`).subscribe((message: IMessage) => {
      console.log("coucou websocket: " + message.body);
    });
    // this.rxStompService.watch(`/topic/hello`).subscribe((message: IMessage) => {
    //   console.log("coucou websocket: " + message.body);
    // });
  }

  private ngAfterViewChecked() {
    this.scrollLogsToBottom()
  }

  private scrollLogsToBottom() {
    let element = document.getElementById("messagesContainer")!;
    element.scrollTop = element.scrollHeight
  }

  private updateUser = (updatedUser: UserDto) => {
    this.user = updatedUser
  }

  testWs() {
    this.httpClient.get(`http://localhost:8080/games/test-ws/${this.gameId}`).subscribe(value => {
      console.log(`received ${value}`);
    })
  }
}
