import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {DrawQuestionComponent} from "../draw-question/draw-question.component";
import {NgIf} from "@angular/common";
import {Identifiable} from "../../../shared/identifiable";

@Component({
  selector: 'app-roll-dice-results-outside-penalty-box',
  standalone: true,
  imports: [
    DrawQuestionComponent,
    NgIf
  ],
  templateUrl: './roll-dice-results-outside-penalty-box.component.html',
  styleUrl: './roll-dice-results-outside-penalty-box.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RollDiceResultsOutsidePenaltyBoxComponent extends Identifiable{
  @Input() gameId!: number;
  @Input() playerId!: string;
  @Input() displayMessage: string | undefined;
}
