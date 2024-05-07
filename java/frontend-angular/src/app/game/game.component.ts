import {Component, ElementRef, Renderer2} from '@angular/core';
import {GameResponseDto} from "../openapi-generated";
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
  protected playerName!: string;
  private gameId!: number;
  protected game!: GameResponseDto;
  protected logs: Array<string> = [...Array(20).keys()].map(value => `message: ${value}`);
  private counter: number = 0;

  constructor(private route: ActivatedRoute,
              private localStorageService: LocalStorageService) {
    this.playerName = localStorage.getItem('playerName')!;
    this.route.params.subscribe(value => {
      this.gameId = value['id'];
    })
    localStorageService.registerObserver(this.updateName)
  }

  protected isCurrentPlayer() {
    return this.game.currentPlayer.name === this.playerName
  }

  protected newMessage() {
    this.logs.push(`coucou ${this.counter++}`)
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

  private updateName = (newName: string) => {
    this.playerName = newName
  }
}
