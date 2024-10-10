import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AsyncPipe, NgClass, NgForOf, NgIf} from "@angular/common";
import {ObjectAttributePipe} from "../../common/object-attribute.pipe";
import {GotoGameButtonComponent} from "../goto-game-button/goto-game-button.component";
import {StartGameButtonComponent} from "../start-game-button/start-game-button.component";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {catchError, Observable, Subject, Subscription} from "rxjs";
import {Game} from "../game";
import {ConsoleLogPipe} from "../../console-log.pipe";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorDisplayComponent} from "../error-display/error-display.component";
import {Identifiable} from "../../common/identifiable";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {GoBackButtonComponent} from "../../common/go-back-button/go-back-button.component";
import {OpenJoinGameDialogComponent} from "../join-game-button/open-join-game-dialog.component";

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [
    OpenJoinGameDialogComponent,
    NgForOf,
    NgIf,
    ObjectAttributePipe,
    GotoGameButtonComponent,
    StartGameButtonComponent,
    AsyncPipe,
    NgClass,
    ConsoleLogPipe,
    ErrorDisplayComponent,
    GoBackButtonComponent,
    OpenJoinGameDialogComponent
  ],
  templateUrl: './game-details.component.html',
  styleUrl: './game-details.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GameDetailsComponent extends Identifiable implements OnDestroy{
  gameId!: number;
  game$!: Observable<Game>;
  private routeParamsSubscription?: Subscription;
  gameLoadingError$= new Subject<HttpErrorResponse>();

  constructor(private route: ActivatedRoute,
              protected router: Router,
              protected gameService: GameServiceAbstract,
              protected userService: UserServiceAbstract) {
    super()
    this.routeParamsSubscription = this.route.params.subscribe(value => {
      this.gameId = Number.parseInt(value['id']);
      this.game$ = gameService.getGame(this.gameId)
        .pipe(
          catchError(err => {
            this.gameLoadingError$.next(err);
            throw err;
          })
        );
    });
  }

  ngOnDestroy(): void {
    this.routeParamsSubscription?.unsubscribe();
  }

}

