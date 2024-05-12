import {Component} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {JoinGameButtonComponent} from "../join-game-button/join-game-button.component";
import {GameResponseDto} from "../openapi-generated";
import {NgForOf, NgIf} from "@angular/common";
import {ObjectAttributePipe} from "../object-attribute.pipe";
import {GotoGameButtonComponent} from "../goto-game-button/goto-game-button.component";
import {StartGameButtonComponent} from "../start-game-button/start-game-button.component";

@Component({
  selector: 'app-game',
  standalone: true,
    imports: [
        JoinGameButtonComponent,
        NgForOf,
        NgIf,
        ObjectAttributePipe,
        GotoGameButtonComponent,
        StartGameButtonComponent
    ],
  templateUrl: './game-details.component.html',
  styleUrl: './game-details.component.css'
})
export class GameDetailsComponent {
  gameId!: number;
  game!: GameResponseDto;
  // TODO localstorage events: synchro le nom si changé dans une autre tab - après gestion d'identité propre
  playerName!: string;

  constructor(private route: ActivatedRoute) {
    this.route.params.subscribe(value => {
      this.gameId = value['id'];
    })
  }

  ngOnInit() {
    this.game = history?.state
  }

  updateGameWith($event: GameResponseDto) {
    this.game = $event
  }
}
