import {ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Game} from "../../game";
import {Player} from "../../../user/player";
import {DrawQuestionComponent} from "../draw-question/draw-question.component";
import {NgIf} from "@angular/common";
import {Identifiable} from "../../../common/identifiable";

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
  @Input() canShowComponent: boolean | undefined;
  @Input() displayMessage: string | undefined;
}
