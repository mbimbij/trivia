import {Component, ElementRef, Renderer2} from '@angular/core';
import {GameResponseDto, UserDto} from "../openapi-generated";
import {ActivatedRoute} from "@angular/router";
import {NgForOf} from '@angular/common';
import {LocalStorageService} from "../local-storage.service";

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
              private localStorageService: LocalStorageService) {
    this.user = localStorageService.getUser()
    console.log(`${this.constructor.name} - create with user ${this.user}`)
    this.route.params.subscribe(value => {
      this.gameId = value['id'];
    })
    localStorageService.registerUserUpdatedObserver(this.updateUser)
  }

  protected isCurrentPlayer() {
    return this.game.currentPlayer === this.user
  }

  protected newMessage() {
    this.logs.push(`newMessage ${this.counter++}`)
  }

  private ngOnInit() {
    this.game = history?.state
  }

  private ngAfterViewChecked() {
    this.scrollLogsToBottom()
  }

  private scrollLogsToBottom() {
    let element = document.getElementById("messagesContainer")!;
    element.scrollTop = element.scrollHeight
  }

  private updateUser = (updatedUser: UserDto) => {
    console.log(`${this.constructor.name} - update user ${this.user}`)
    this.user = updatedUser
  }
}
