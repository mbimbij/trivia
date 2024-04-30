import {Component} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {JoinGameButtonComponent} from "../join-game-button/join-game-button.component";
import {GameResponseDto} from "../openapi-generated";
import {NgForOf, NgIf} from "@angular/common";
import {ObjectAttributePipe} from "../object-attribute.pipe";

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [
    JoinGameButtonComponent,
    NgForOf,
    NgIf,
    ObjectAttributePipe
  ],
  templateUrl: './game.component.html',
  styleUrl: './game.component.css'
})
export class GameComponent {
  gameId!: number;
  game!: GameResponseDto;
  // TODO localstorage events: synchro le nom si changé dans une autre tab - après gestion d'identité propre
  playerName!: string;

  constructor(private route: ActivatedRoute) {
    this.route.params.subscribe(value => {
      this.gameId = value['id'];
      // this.gameId = 777;
    })
  }

  ngOnInit() {
    this.game = history?.state
  }

  updateGameWith($event: GameResponseDto) {
    this.game = $event
  }
}
