import {Component} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {JoinGameButtonComponent} from "../join-game-button/join-game-button.component";
import {GameResponseDto} from "../../openapi-generated";
import {NgForOf, NgIf} from "@angular/common";
import {ObjectAttributePipe} from "../../common/object-attribute.pipe";
import {GotoGameButtonComponent} from "../goto-game-button/goto-game-button.component";
import {StartGameButtonComponent} from "../start-game-button/start-game-button.component";
import {GameServiceAbstract} from "../game-service-abstract";

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
  protected dataLoaded: boolean = false;

  constructor(private route: ActivatedRoute,
              private gameService: GameServiceAbstract) {
    this.route.params.subscribe(value => {
      this.gameId = value['id'];
    })
  }

  ngOnInit() {
    this.gameService.getGame(this.gameId)
      .subscribe(value => {
        this.game = value
        this.dataLoaded = true;
      })
    this.gameService.registerGameUpdatedObserver(this.gameId, this.updateGameWithArrow);
  }

  updateGameWith($event: GameResponseDto) {
    this.game = $event
  }

  protected updateGameWithArrow = (replacement: GameResponseDto) => {
    this.updateGameWith(replacement);
  }
}
