import {ChangeDetectionStrategy, Component, Input, OnDestroy} from '@angular/core';
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {Subscription} from "rxjs";
import {Identifiable} from "../../common/identifiable";
import {AsyncPipe} from "@angular/common";

@Component({
  selector: 'app-delete-game-button',
  standalone: true,
  imports: [
    AsyncPipe
  ],
  template: `
    <button [attr.data-testid]="'delete-button-'+gameId"
            [disabled]="!canDelete"
            (click)="deleteGame()">
      delete
    </button>
    {{ checkRender() }}
  `,
  styleUrl: './delete-game-button.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DeleteGameButtonComponent extends Identifiable implements OnDestroy {
  @Input() gameId!: number;
  @Input() userId!: string;
  @Input() canDelete!: boolean;
  private actionSubscription?: Subscription;

  constructor(private gameService: GameServiceAbstract) {
    super()
  }

  ngOnDestroy(): void {
    this.actionSubscription?.unsubscribe();
  }

  protected deleteGame() {
    this.actionSubscription = this.gameService.delete(this.gameId).subscribe();
  }
}
