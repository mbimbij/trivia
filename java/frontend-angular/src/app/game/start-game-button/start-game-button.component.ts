import {ChangeDetectionStrategy, Component, Input, OnChanges, OnDestroy, SimpleChanges,} from '@angular/core';
import {AsyncPipe, NgIf} from "@angular/common";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {Game} from "../game";
import {BehaviorSubject, Subscription} from "rxjs";
import {Identifiable} from "../../common/identifiable";
import {State} from "../../openapi-generated/game";

@Component({
  standalone: true,
  selector: 'app-start-game-button',
  imports: [
    NgIf,
    AsyncPipe
  ],
  template: `
    <button [attr.data-testid]="'start-button-'+gameId" (click)="startGame()" *ngIf="canStart">
      start
    </button>
    {{ checkRender() }}
  `,
  styleUrl: './start-game-button.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class StartGameButtonComponent extends Identifiable implements OnDestroy {

  @Input() gameId!: number
  @Input() userId!: string;
  @Input() canStart!: boolean;

  private actionSubscription: Subscription | undefined;

  constructor(private gameService: GameServiceAbstract) {
    super()
  }

  ngOnDestroy() {
    this.actionSubscription?.unsubscribe()
  }

  startGame() {
    this.actionSubscription = this.gameService.start(this.gameId, this.userId)
      .subscribe()
  }
}
