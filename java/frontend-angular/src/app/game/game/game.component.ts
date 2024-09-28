import {AfterViewChecked, ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AsyncPipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {userToPlayer} from "../../common/helpers";
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
  RollDiceResultsInsidePenaltyBoxComponent
} from "./roll-dice-results-inside-penalty-box/roll-dice-results-inside-penalty-box.component";
import {
  RollDiceResultsOutsidePenaltyBoxComponent
} from "./roll-dice-results-outside-penalty-box/roll-dice-results-outside-penalty-box.component";
import {
  AnswerQuestionResultsComponent
} from "./answer-question-results-wrapper/answer-question-results/answer-question-results.component";
import {
  AnswerQuestionResultsWrapperComponent
} from "./answer-question-results-wrapper/answer-question-results-wrapper.component";
import {Identifiable} from "../../common/identifiable";
import {GameLog} from "../../openapi-generated/gamelogs";
import {GameLogsServiceAbstract} from "../../services/gamelogs-service-abstract";
import {MessageService} from "../../services/message.service";
import {GoBackButtonComponent} from "../../common/go-back-button/go-back-button.component";

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
    RollDiceResultsInsidePenaltyBoxComponent,
    RollDiceResultsOutsidePenaltyBoxComponent,
    AnswerQuestionResultsComponent,
    AnswerQuestionResultsWrapperComponent,
    GoBackButtonComponent
  ],
  templateUrl: './game.component.html',
  styleUrl: './game.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GameComponent extends Identifiable implements OnDestroy, AfterViewChecked {
  protected player!: Player;
  protected gameId!: number;
  game$!: Observable<Game>
  protected gameLogs$!: Observable<GameLog[]>;
  protected isGameEnded: boolean = false;
  gameLoadingError$ = new Subject<HttpErrorResponse>();

  private userGameSubscription: Subscription | undefined;
  private routeParamsSubscription: Subscription;

  constructor(private route: ActivatedRoute,
              private userService: UserServiceAbstract,
              private gameService: GameServiceAbstract,
              private gameLogsService: GameLogsServiceAbstract,
              protected msgService: MessageService) {
    super()
    this.routeParamsSubscription = this.route.params.subscribe(value => {
      this.gameId = Number.parseInt(value['id']);

      this.gameLogsService.initGameLogs(this.gameId);
      this.gameLogs$ = this.gameLogsService.getGameLogs(this.gameId);

      this.game$ = this.gameService.getGame(this.gameId);
      let user$ = this.userService.getUser();

      this.userGameSubscription = combineLatest([user$, this.game$])
        .subscribe({
          next: ([user, game]) => {
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
