import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {AsyncPipe, NgClass, NgForOf, NgIf} from "@angular/common";
import {ObjectAttributePipe} from "../../shared/object-attribute.pipe";
import {GotoGameButtonComponent} from "../goto-game-button/goto-game-button.component";
import {StartGameButtonComponent} from "../start-game-button/start-game-button.component";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {catchError, Observable, Subject, Subscription} from "rxjs";
import {Game} from "../game";
import {ConsoleLogPipe} from "../../console-log.pipe";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorDisplayComponent} from "../error-display/error-display.component";
import {Identifiable} from "../../shared/identifiable";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {GoBackButtonComponent} from "../../shared/go-back-button/go-back-button.component";
import {OpenJoinGameDialogComponent} from "../join-game-button/open-join-game-dialog.component";
import {GameListDataSource} from "../../shared/game-list-data.source";
import {DeleteGameButtonComponent} from "../delete-game-button/delete-game-button.component";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow, MatRowDef, MatTable
} from "@angular/material/table";

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
    OpenJoinGameDialogComponent,
    DeleteGameButtonComponent,
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    MatTable,
    MatHeaderCellDef,
    RouterLink
  ],
  templateUrl: './game-details.component.html',
  styleUrl: './game-details.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GameDetailsComponent extends Identifiable implements OnDestroy{
  gameId!: number;
  game$!: Observable<Game>;
  private routeParamsSubscription?: Subscription;
  public gameLoadingError$= new Subject<HttpErrorResponse>();
  protected displayedColumns = ['id', 'name', 'creator', 'players', 'state', 'join', 'start', 'goto']
  protected dataSource = new GameListDataSource();

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
      this.game$.subscribe(game => {
        this.dataSource.setData([game])
      })
    });
  }

  ngOnDestroy(): void {
    this.routeParamsSubscription?.unsubscribe();
  }

}

