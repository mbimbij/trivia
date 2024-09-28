import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {ValidationButtonComponent} from "../validation-button/validation-button.component";
import {NgIf} from "@angular/common";
import {Identifiable} from "../../../common/identifiable";

@Component({
  selector: 'app-roll-dice-results-inside-penalty-box',
  standalone: true,
  imports: [
    ValidationButtonComponent,
    NgIf
  ],
  templateUrl: './roll-dice-results-inside-penalty-box.component.html',
  styleUrl: './roll-dice-results-inside-penalty-box.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RollDiceResultsInsidePenaltyBoxComponent extends Identifiable {
  @Input() gameId!: number;
  @Input() playerId!: string;
  @Input() displayMessage: string | undefined;
}
