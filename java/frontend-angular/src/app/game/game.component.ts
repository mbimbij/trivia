import { Component } from '@angular/core';
import {GameResponseDto} from "../openapi-generated";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [],
  templateUrl: './game.component.html',
  styleUrl: './game.component.css'
})
export class GameComponent {
  gameId!: number;
  game!: GameResponseDto;

  constructor(private route: ActivatedRoute) {
    this.route.params.subscribe(value => {
      this.gameId = value['id'];
    })
  }

  ngOnInit() {
    this.game = history?.state
  }

  protected readonly JSON = JSON;
}
