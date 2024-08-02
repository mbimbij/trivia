import {AfterViewChecked, ChangeDetectionStrategy, Component, OnDestroy, OnInit} from '@angular/core';
import {GameLog} from "../../openapi-generated";
import {ActivatedRoute, Router} from "@angular/router";
import {AsyncPipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {comparePlayers, generateRandomString, userToPlayer} from "../../common/helpers";
import {Player} from "../../user/player";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {combineLatest, Observable, of, Subject, Subscription} from "rxjs";
import {Game} from "../game";
import {ConsoleLogPipe} from "../../console-log.pipe";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {RollDiceComponent} from "./roll-dice/roll-dice.component";
import {AnswerQuestionComponent} from "./answer-question/answer-question.component";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorDisplayComponent} from "../error-display/error-display.component";
import {
    RollDiceBackhandInsidePenaltyBoxComponent
} from "./roll-dice-backhand-inside-penalty-box/roll-dice-backhand-inside-penalty-box.component";
import {
    RollDiceBackhandOutsidePenaltyBoxComponent
} from "./roll-dice-backhand-outside-penalty-box/roll-dice-backhand-outside-penalty-box.component";

@Component({
  selector: 'app-game',
  standalone: true,
    imports: [
        NgForOf,
        NgIf,
        AsyncPipe,
        ConsoleLogPipe,
        RollDiceComponent,
        AnswerQuestionComponent,
        NgClass,
        ErrorDisplayComponent,
        RollDiceBackhandInsidePenaltyBoxComponent,
        RollDiceBackhandOutsidePenaltyBoxComponent
    ],
  templateUrl: './game.component.html',
  styleUrl: './game.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GameComponent implements OnDestroy, OnInit, AfterViewChecked {
  private readonly id: string;
  protected player!: Player;
  protected gameId!: number;
  private game!: Game;
  game$!: Observable<Game>
  protected gameLogs$!: Observable<GameLog[]>;
  protected isGameEnded: boolean = false;
  gameLoadingError$= new Subject<HttpErrorResponse>();

  private userGameSubscription: Subscription | undefined;
  private routeParamsSubscription: Subscription;

  constructor(private route: ActivatedRoute,
              protected router: Router,
              private userService: UserServiceAbstract,
              private gameService: GameServiceAbstract) {
    this.id = `${this.constructor.name} - ${generateRandomString(4)}`;
    // console.log(`constructor ${this.id} called`)
    this.routeParamsSubscription = this.route.params.subscribe(value => {
      this.gameId = Number.parseInt(value['id']);

      this.game$ = this.gameService.getGame(this.gameId);
      let user$ = this.userService.getUser();

      this.userGameSubscription = combineLatest([user$, this.game$])
        .subscribe({
          next: ([user, game]) => {
            this.game = game;
            let playerFromUser = userToPlayer(user);
            this.player = game.getCurrentStateOf(playerFromUser);
            this.isGameEnded = game.isEnded();
          },
          error: err => {
            this.gameLoadingError$.next(err);
          }
        });
    });
  }

  ngOnInit() {
    this.gameService.initGameLogs(this.gameId);
    this.gameLogs$ = this.gameService.getGameLogs(this.gameId);
  }

  ngAfterViewChecked() {
    this.scrollLogsToBottom()
  }

  ngOnDestroy(): void {
    this.userGameSubscription?.unsubscribe();
    this.routeParamsSubscription.unsubscribe();
  }

  private scrollLogsToBottom() {
    let element = document.getElementById("messagesContainer")!;
    if (element) {
      element.scrollTop = element.scrollHeight
    }
  }

  protected playerWon(): boolean {
    return comparePlayers(this.player, this.game.winner)
  }

  /**
   * For tests only
   * @param player
   */
  setPlayer(player: Player) {
    this.player = player
  }

  /**
   * For tests only
   * @param game
   */
  setGame(game: Game) {
    this.game$ = of(game)
  }
}
