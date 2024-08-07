import {Component, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {JoinGameButtonComponent} from "../join-game-button/join-game-button.component";
import {AsyncPipe, NgClass, NgForOf, NgIf} from "@angular/common";
import {ObjectAttributePipe} from "../../common/object-attribute.pipe";
import {GotoGameButtonComponent} from "../goto-game-button/goto-game-button.component";
import {StartGameButtonComponent} from "../start-game-button/start-game-button.component";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {catchError, Observable, Subject, Subscription} from "rxjs";
import {Game} from "../game";
import {generateRandomString} from "../../common/helpers";
import {ConsoleLogPipe} from "../../console-log.pipe";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorDisplayComponent} from "../error-display/error-display.component";
import {Identifiable} from "../../common/identifiable";

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [
    JoinGameButtonComponent,
    NgForOf,
    NgIf,
    ObjectAttributePipe,
    GotoGameButtonComponent,
    StartGameButtonComponent,
    AsyncPipe,
    NgClass,
    ConsoleLogPipe,
    ErrorDisplayComponent
  ],
  templateUrl: './game-details.component.html',
  styleUrl: './game-details.component.css'
})
export class GameDetailsComponent extends Identifiable implements OnDestroy{
  gameId!: number;
  game$!: Observable<Game>;
  private routeParamsSubscription?: Subscription;
  gameLoadingError$= new Subject<HttpErrorResponse>();

  constructor(private route: ActivatedRoute,
              protected router: Router,
              private gameService: GameServiceAbstract) {
    super()
    // console.log(`constructor ${this.id} called`)
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

  ngOnInit(): void {
    // console.log(`ngOnInit ${this.constructor.name} - ${this.id} called`)
  }

  ngOnDestroy(): void {
    // console.log(`ngOnDestroy ${this.constructor.name} - ${this.id} called`)
    this.routeParamsSubscription?.unsubscribe();
  }

}

