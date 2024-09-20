import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {Identifiable} from "../../../common/identifiable";

@Component({
  selector: 'app-validation-button',
  standalone: true,
  imports: [],
  template: `
    <button
      [attr.data-testid]="'validate'"
      (click)="validate(gameId, playerId)">{{ buttonText || 'ok' }}
    </button>
    {{ checkRender() }}
  `,
  styleUrl: './validation-button.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ValidationButtonComponent extends Identifiable {
  @Input() gameId!: number;
  @Input() playerId!: string;
  @Input() buttonText: string | undefined;

  constructor(private gameService: GameServiceAbstract) {
    super()
  }

  protected validate(gameId: number, playerId: string): void {
    this.gameService.validate(gameId, playerId).subscribe()
  }
}
