import {ChangeDetectionStrategy, Component, Input, OnDestroy,} from '@angular/core';
import {AsyncPipe, NgIf} from "@angular/common";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {Subscription} from "rxjs";
import {Identifiable} from "../../shared/identifiable";
import {MatButton} from "@angular/material/button";

@Component({
  standalone: true,
  selector: 'app-start-game-button',
  imports: [
    NgIf,
    AsyncPipe,
    MatButton
  ],
  template: `
    <button
      mat-stroked-button
      class="rounded"
      [attr.data-testid]="'start-button-'+gameId"
      [disabled]="!canStart"
      (click)="startGame()">
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
