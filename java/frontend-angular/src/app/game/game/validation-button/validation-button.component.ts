import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {GameServiceAbstract} from "../../../services/game-service-abstract";

@Component({
  selector: 'app-validation-button',
  standalone: true,
  imports: [],
  template: `
    <button
      [attr.data-testid]="'validate'"
      (click)="validate(gameId, playerId)">{{ buttonText || 'ok' }}
    </button>
  `,
  styleUrl: './validation-button.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ValidationButtonComponent {
  @Input() gameId!: number;
  @Input() playerId!: string;
  @Input() buttonText: string | undefined;

  constructor(private gameService: GameServiceAbstract) {
  }

  protected validate(gameId: number, playerId: string): void {
    this.gameService.validate(gameId, playerId).subscribe()
  }
}
